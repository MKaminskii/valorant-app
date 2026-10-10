# Baixa UMA VEZ os dados e as imagens de todos os agentes jogaveis para dentro do projeto.
# O app NAO acessa a internet:
#   - os textos viram o arquivo de mocks (MockAgents.kt), gerado a partir de scripts/agents-pt-BR.json;
#   - as imagens ficam empacotadas em app/src/main/res/drawable-nodpi.
# Como rodar (na pasta do projeto):
#   powershell -ExecutionPolicy Bypass -File .\scripts\baixar-imagens.ps1

$ErrorActionPreference = 'Stop'
$ProgressPreference = 'SilentlyContinue'

$destino = Join-Path $PSScriptRoot '..\app\src\main\res\drawable-nodpi'
New-Item -ItemType Directory -Force -Path $destino | Out-Null

# 1) Lista de agentes em portugues (salva para gerar os mocks)
$json = Join-Path $PSScriptRoot 'agents-pt-BR.json'
Invoke-WebRequest -Uri 'https://valorant-api.com/v1/agents?isPlayableCharacter=true&language=pt-BR' -OutFile $json -UseBasicParsing
$agentes = (Get-Content $json -Raw -Encoding UTF8 | ConvertFrom-Json).data
Write-Host "Agentes encontrados: $($agentes.Count)"

# Nome do arquivo: so letras minusculas e numeros (ex.: KAY/O -> kayo)
function Slug([string]$nome) { return ($nome.ToLower() -replace '[^a-z0-9]', '') }

# Reduz a imagem para no maximo $max pixels, deixando o app mais leve.
function Reduzir([string]$caminho, [int]$max) {
    try {
        Add-Type -AssemblyName System.Drawing
        $img = [System.Drawing.Image]::FromFile($caminho)
        if ($img.Width -le $max -and $img.Height -le $max) { $img.Dispose(); return }
        $escala = [Math]::Min($max / $img.Width, $max / $img.Height)
        $w = [int]($img.Width * $escala)
        $h = [int]($img.Height * $escala)
        $bmp = New-Object System.Drawing.Bitmap $w, $h
        $g = [System.Drawing.Graphics]::FromImage($bmp)
        $g.InterpolationMode = [System.Drawing.Drawing2D.InterpolationMode]::HighQualityBicubic
        $g.DrawImage($img, 0, 0, $w, $h)
        $g.Dispose(); $img.Dispose()
        $bmp.Save("$caminho.tmp", [System.Drawing.Imaging.ImageFormat]::Png)
        $bmp.Dispose()
        Move-Item -Force "$caminho.tmp" $caminho
    } catch {
        Write-Host "   (nao foi possivel reduzir; mantido no tamanho original)"
    }
}

function Baixar([string]$url, [string]$arquivo, [int]$tamanhoMaximo) {
    if (-not $url) { return }
    $saida = Join-Path $destino $arquivo
    if (Test-Path $saida) { return }   # ja baixado antes
    Invoke-WebRequest -Uri $url -OutFile $saida -UseBasicParsing
    if ($tamanhoMaximo -gt 0) { Reduzir $saida $tamanhoMaximo }
    Write-Host "OK  $arquivo"
}

# 2) Imagens de cada agente (icone, retrato e arte de fundo) e de suas habilidades
foreach ($agente in $agentes) {
    $nome = Slug $agente.displayName
    Baixar $agente.displayIcon "agent_icon_$nome.png" 256
    Baixar ($(if ($agente.fullPortraitV2) { $agente.fullPortraitV2 } else { $agente.fullPortrait })) "agent_portrait_$nome.png" 640
    Baixar $agente.background "agent_background_$nome.png" 640
    foreach ($habilidade in $agente.abilities) {
        if ($habilidade.slot -eq 'Passive') { continue }
        $slot = $habilidade.slot.ToLower()
        Baixar $habilidade.displayIcon "ability_${nome}_$slot.png" 0
    }
}

Write-Host ""
Write-Host "Pronto: $((Get-ChildItem $destino -Filter *.png).Count) imagens em $((Resolve-Path $destino).Path)"

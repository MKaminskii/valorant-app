"""Gera app/.../data/mock/MockAgents.kt a partir de scripts/agents-pt-BR.json.

O JSON é baixado uma única vez pelo baixar-imagens.ps1. O app não lê este JSON nem acessa
a internet: ele usa apenas o arquivo Kotlin gerado (dados simulados).

Uso (na pasta do projeto):  python scripts/gerar-mocks.py
"""
import json
import os
import re

RAIZ = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..")
JSON = os.path.join(RAIZ, "scripts", "agents-pt-BR.json")
IMAGENS = os.path.join(RAIZ, "app", "src", "main", "res", "drawable-nodpi")
SAIDA = os.path.join(
    RAIZ, "app", "src", "main", "java", "com", "mkaminskii", "valorantagents",
    "data", "mock", "MockAgents.kt",
)

# Nome da variável Kotlin de cada função, pelo id da API.
FUNCOES = {
    "dbe8757e-9e92-4ed4-b39f-9dfc589691d4": "duelist",
    "1b47567f-8f7b-444b-aae3-b0c634622d10": "initiator",
    "5fc02f99-4091-4486-a531-98459a3e95e9": "sentinel",
    "4ee40330-ecdd-4f2f-98a8-eb1243428373": "controller",
}
SLOTS = {
    "Grenade": "GRENADE",
    "Ability1": "ABILITY_1",
    "Ability2": "ABILITY_2",
    "Ultimate": "ULTIMATE",
}
ORDEM_SLOTS = list(SLOTS)

# Nomes reais segundo a história do jogo (pesquisados em sites de lore de Valorant).
# Agentes que não estão aqui não têm nome real revelado: o campo fica nulo.
NOMES_REAIS = {
    "Astra": "Efia Danso",
    "Breach": "Erik Torsten",
    "Brimstone": "Liam Byrne",
    "Chamber": "Vincent Fabron",
    "Cypher": "Amir El Amari",
    "Deadlock": "Iselin",
    "Fade": "Hazal Eyletmez",
    "Gekko": "Mateo Armendáriz de la Fuente",
    "Harbor": "Varun Batra",
    "Iso": "Li Zhao Yu",
    "Jett": "Sunwoo Han",
    "Killjoy": "Klara Böhringer",
    "Neon": "Tala Nicole Dimaapi Valdez",
    "Phoenix": "Jamie Adeyemi",
    "Raze": "Tayane Alves",
    "Reyna": "Zyanya Mondragón",
    "Sage": "Ling Ying Wei",
    "Skye": "Kirra Foster",
    "Sova": "Sasha Novikov",
    "Viper": "Sabine Callas",
    "Yoru": "Ryo Kiritani",
}


def slug(nome):
    return re.sub(r"[^a-z0-9]", "", nome.lower())


def texto(valor):
    """String Kotlin entre aspas, com espaços normalizados e caracteres escapados."""
    valor = re.sub(r"\s+", " ", valor or "").strip()
    valor = valor.replace("\\", "\\\\").replace('"', '\\"').replace("$", "\\$")
    return f'"{valor}"'


def drawable(nome):
    """Referência R.drawable.x se a imagem existir; senão null (campo opcional)."""
    if os.path.exists(os.path.join(IMAGENS, nome + ".png")):
        return f"R.drawable.{nome}"
    return "null"


def cor(gradiente):
    if not gradiente:
        return "null"
    valor = gradiente[0]
    if not re.fullmatch(r"[0-9a-fA-F]{8}", valor or ""):
        return "null"
    return f'"#{valor[:6].upper()}"'


def main():
    with open(JSON, encoding="utf-8-sig") as arquivo:
        dados = json.load(arquivo)["data"]

    agentes = {}
    for agente in dados:
        if agente.get("isPlayableCharacter") and agente.get("displayName"):
            agentes[agente["uuid"]] = agente
    agentes = sorted(agentes.values(), key=lambda a: a["displayName"].lower())

    funcoes = {}
    for agente in agentes:
        funcao = agente.get("role")
        if funcao:
            funcoes.setdefault(funcao["uuid"], funcao)

    linhas = [
        "package com.mkaminskii.valorantagents.data.mock",
        "",
        "import com.mkaminskii.valorantagents.R",
        "import com.mkaminskii.valorantagents.model.Ability",
        "import com.mkaminskii.valorantagents.model.AbilitySlot",
        "import com.mkaminskii.valorantagents.model.Agent",
        "import com.mkaminskii.valorantagents.model.AgentRole",
        "",
        "/**",
        " * Gerado por scripts/gerar-mocks.py a partir dos textos oficiais em português da Valorant API.",
        " */",
        "object MockAgents {",
        "",
    ]

    nomes_funcoes = {}
    for uuid, funcao in funcoes.items():
        variavel = FUNCOES.get(uuid, "role" + slug(funcao["displayName"]).capitalize())
        nomes_funcoes[uuid] = variavel
        linhas += [
            f"    private val {variavel} = AgentRole(",
            f'        id = "{uuid}",',
            f"        name = {texto(funcao['displayName'])},",
            f"        description = {texto(funcao.get('description'))},",
            "    )",
        ]
    linhas += ["", "    val agents: List<Agent> = listOf("]

    for agente in agentes:
        nome = slug(agente["displayName"])
        funcao = agente.get("role")
        habilidades = sorted(
            (h for h in agente.get("abilities") or [] if h.get("slot") in SLOTS and h.get("displayName")),
            key=lambda h: ORDEM_SLOTS.index(h["slot"]),
        )
        tags = [t for t in (agente.get("characterTags") or []) if t]
        nome_real = NOMES_REAIS.get(agente["displayName"])

        linhas += [
            "        Agent(",
            f'            id = "{agente["uuid"]}",',
            f"            name = {texto(agente['displayName'])},",
            f"            description = {texto(agente.get('description'))},",
            f"            realName = {texto(nome_real) if nome_real else 'null'},",
            f"            role = {nomes_funcoes[funcao['uuid']] if funcao else 'null'},",
            "            abilities = listOf(",
        ]
        for habilidade in habilidades:
            slot = habilidade["slot"]
            linhas += [
                "                Ability(",
                f"                    slot = AbilitySlot.{SLOTS[slot]},",
                f"                    name = {texto(habilidade['displayName'])},",
                f"                    description = {texto(habilidade.get('description'))},",
                f"                    iconRes = {drawable(f'ability_{nome}_{slot.lower()}')},",
                "                ),",
            ]
        linhas += [
            "            ),",
            f"            tags = {('listOf(' + ', '.join(texto(t) for t in tags) + ')') if tags else 'null'},",
            f"            colorHex = {cor(agente.get('backgroundGradientColors'))},",
            f"            iconRes = {drawable(f'agent_icon_{nome}')},",
            f"            portraitRes = {drawable(f'agent_portrait_{nome}')},",
            f"            backgroundRes = {drawable(f'agent_background_{nome}')},",
            "        ),",
        ]

    linhas += [
        "    )",
        "",
        "    /** Funções distintas presentes nos mocks, na ordem em que aparecem. */",
        "    val roles: List<AgentRole>",
        "        get() = agents.mapNotNull { it.role }.distinctBy { it.id }",
        "",
        "    fun findById(id: String?): Agent? = agents.firstOrNull { it.id == id }",
        "}",
        "",
    ]

    with open(SAIDA, "w", encoding="utf-8", newline="\n") as arquivo:
        arquivo.write("\n".join(linhas))

    sem_imagem = [a["displayName"] for a in agentes if drawable(f"agent_icon_{slug(a['displayName'])}") == "null"]
    print(f"{len(agentes)} agentes e {len(funcoes)} funções gerados em MockAgents.kt")
    print("Sem nome real:", ", ".join(a["displayName"] for a in agentes if a["displayName"] not in NOMES_REAIS))
    if sem_imagem:
        print("Sem ícone (usarão a inicial):", ", ".join(sem_imagem))


if __name__ == "__main__":
    main()

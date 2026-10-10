# Valorant Agentes

Aplicativo Android para consultar os agentes de **Valorant**: a função de cada um, a biografia e as habilidades de cada tecla (C, Q, E e X).

Projeto individual da disciplina de Programação Mobile (AC322B), etapa **Parcial: Android Views (XML) e navegação + Intent**. Nesta etapa os dados são simulados (mocks): não há acesso à internet nem banco de dados. Os textos ficam em `MockAgents.kt` e as imagens dos agentes ficam empacotadas no próprio app.

## Telas

1. **Lista de agentes** (`AgentsActivity`): lista com `RecyclerView`, com o ícone de cada agente sobre a cor dele, e um filtro por função feito com chips. Tocar em um chip atualiza a lista; tocar em um agente abre o detalhe.
2. **Detalhe do agente** (`AgentDetailActivity`): aberta por `Intent` explícita, que recebe o id do agente. Mostra o retrato do agente sobre a arte de fundo oficial do jogo e um degradê com a cor dele, a função, o nome real, as tags, a biografia, a descrição da função e a seção de habilidades. As habilidades ficam num `Fragment` (`AbilitiesFragment`) dentro desta tela, cada uma com seu ícone; tocar em uma habilidade mostra a descrição dela.

## Requisitos atendidos

| Requisito | Onde |
|---|---|
| Duas telas com layouts XML, Views e ViewGroups | `activity_agents.xml` e `activity_agent_detail.xml` (`TextView`, `ImageView`, `RecyclerView`, `LinearLayout`, `FrameLayout` no cabeçalho e no avatar, `HorizontalScrollView`, `NestedScrollView`, `MaterialCardView`, `FragmentContainerView`) |
| Navegação por `Intent` explícita com passagem de dados | `AgentDetailActivity.newIntent()` envia o id do agente no extra `EXTRA_AGENT_ID` |
| Views conectadas ao Kotlin e interação que atualiza a interface | ViewBinding em todas as telas; o filtro por função atualiza a lista e a seleção de habilidade atualiza a descrição |
| Modelos imutáveis (`data class`) e valores opcionais | `model/Agent.kt`: `role`, `realName`, `tags`, `colorHex` e as imagens (`iconRes`, `portraitRes`, `backgroundRes`) são nullable. Na tela, função ausente vira "Sem função", nome real e tags ausentes ficam ocultos (vários agentes não têm nome real revelado), cor ausente usa a cor do tema, imagem ausente mostra a inicial do nome, e id inválido na Intent fecha o detalhe com um aviso |
| Dados simulados | `data/mock/MockAgents.kt`, com todos os agentes jogáveis e os nomes oficiais em português das habilidades. As imagens estão em `res/drawable-nodpi` |

### Itens opcionais

| Item | Onde |
|---|---|
| Apenas ViewBinding (nenhum `findViewById`) | Todas as Activities, o Adapter e o Fragment |
| Componentes XML reutilizáveis inflados e com eventos que atualizam a interface | `view_agent_avatar`, `view_badge`, `view_filter_chip` (clique filtra a lista) e `item_ability` (clique atualiza a descrição) |
| Interface funcional em `Fragment` com ciclo de vida e ViewBinding | `AbilitiesFragment`: binding criado em `onCreateView` e liberado em `onDestroyView` |

## Como rodar

1. Clone o repositório:
   ```bash
   git clone https://github.com/MKaminskii/valorant-app.git
   ```
2. Abra a pasta no **Android Studio** (versão com suporte ao Android Gradle Plugin 9.3, a mesma usada nas aulas).
3. Aguarde o Gradle sincronizar. O JDK 25 da toolchain é baixado automaticamente (foojay).
4. Execute a configuração `app` em um emulador ou dispositivo com **Android 13 (API 33)** ou superior.

Nenhuma chave de API ou arquivo `.env` é necessário, e o app funciona sem internet.

### De onde vêm os dados

O app usa só dados simulados que já estão no repositório: os textos em `data/mock/MockAgents.kt` e as imagens em `app/src/main/res/drawable-nodpi`. **O app não faz nenhuma requisição.**

Esses mocks foram montados uma única vez a partir dos textos oficiais em português da [Valorant API](https://valorant-api.com), com dois scripts. Eles só precisam ser rodados de novo para atualizar a lista, por exemplo quando sair um agente novo:

```powershell
powershell -ExecutionPolicy Bypass -File .\scripts\baixar-imagens.ps1   # baixa o JSON e as imagens
python scripts\gerar-mocks.py                                         # gera o MockAgents.kt
```

Os nomes reais dos agentes vêm de sites de lore de Valorant. Agentes sem nome real revelado (como Omen e KAY/O) ficam com o campo nulo.

## Estrutura

```
scripts/            # baixar-imagens.ps1 e gerar-mocks.py (usados só uma vez)
app/src/main/java/com/mkaminskii/valorantagents/
├── model/          # data classes (Agent, AgentRole, Ability)
├── data/mock/      # dados simulados (textos), gerados por scripts/gerar-mocks.py
└── views/
    ├── agents/     # tela 1: AgentsActivity e AgentsAdapter
    ├── detail/     # tela 2: AgentDetailActivity e AbilitiesFragment
    └── common/     # extensões compartilhadas (avatar e edge-to-edge)
```

## Bibliotecas externas

| Biblioteca | Uso |
|---|---|
| AndroidX AppCompat / Activity / Core KTX | Base das Activities, edge-to-edge e extensões Kotlin |
| AndroidX RecyclerView | Lista de agentes com `ListAdapter` e `DiffUtil` |
| AndroidX Fragment KTX | `AbilitiesFragment` e a transação com `commit {}` |
| Material Components | Tema Material 3, `MaterialCardView`, `Chip`/`ChipGroup`, `MaterialToolbar` e `ShapeableImageView` (avatar redondo) |

# Valorant Agentes

Aplicativo Android para consultar os agentes de **Valorant**: a função de cada um, a biografia e as habilidades de cada tecla (C, Q, E e X).

Projeto individual da disciplina de Programação Mobile (AC322B), etapa **Parcial: Android Views (XML) e navegação + Intent**. Nesta etapa os dados são simulados (mocks): não há acesso à internet nem banco de dados.

## Telas

1. **Lista de agentes** (`AgentsActivity`): lista com `RecyclerView` e um filtro por função feito com chips. Tocar em um chip atualiza a lista; tocar em um agente abre o detalhe.
2. **Detalhe do agente** (`AgentDetailActivity`): aberta por `Intent` explícita, que recebe o id do agente. Mostra o avatar, a função, o codinome, as tags, a biografia, a descrição da função e a seção de habilidades. As habilidades ficam num `Fragment` (`AbilitiesFragment`) dentro desta tela; tocar em uma habilidade mostra a descrição dela.

## Requisitos atendidos

| Requisito | Onde |
|---|---|
| Duas telas com layouts XML, Views e ViewGroups | `activity_agents.xml` e `activity_agent_detail.xml` (`TextView`, `ImageView`, `RecyclerView`, `LinearLayout`, `FrameLayout` no avatar, `HorizontalScrollView`, `NestedScrollView`, `MaterialCardView`, `FragmentContainerView`) |
| Navegação por `Intent` explícita com passagem de dados | `AgentDetailActivity.newIntent()` envia o id do agente no extra `EXTRA_AGENT_ID` |
| Views conectadas ao Kotlin e interação que atualiza a interface | ViewBinding em todas as telas; o filtro por função atualiza a lista e a seleção de habilidade atualiza a descrição |
| Modelos imutáveis (`data class`) e valores opcionais | `model/Agent.kt`: `role`, `developerName`, `tags` e `colorHex` são nullable. Na tela, função ausente vira "Sem função", codinome e tags ausentes ficam ocultos, cor ausente usa a cor do tema, e id inválido na Intent fecha o detalhe com um aviso |
| Dados simulados | `data/mock/MockAgents.kt` |

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

Nenhuma chave de API ou arquivo `.env` é necessário.

## Estrutura

```
app/src/main/java/com/mkaminskii/valorantagents/
├── model/          # data classes (Agent, AgentRole, Ability)
├── data/mock/      # dados simulados
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
| Material Components | Tema Material 3, `MaterialCardView`, `Chip`/`ChipGroup` e `MaterialToolbar` |

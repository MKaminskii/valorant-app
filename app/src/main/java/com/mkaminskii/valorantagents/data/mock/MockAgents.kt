package com.mkaminskii.valorantagents.data.mock

import com.mkaminskii.valorantagents.model.Ability
import com.mkaminskii.valorantagents.model.AbilitySlot
import com.mkaminskii.valorantagents.model.Agent
import com.mkaminskii.valorantagents.model.AgentRole

/**
 * Dados simulados (mocks). Nenhuma chamada de rede é feita: tudo vem desta lista em memória.
 * Alguns agentes não têm todos os campos opcionais (tags, codinome ou cor),
 * para que o tratamento de valores nulos apareça na interface.
 */
object MockAgents {

    private val duelist = AgentRole(
        id = "dbe8757e-9e92-4ed4-b39f-9dfc589691d4",
        name = "Duelista",
        description = "Agentes que buscam o confronto e abrem espaço para o time entrar no bomb.",
    )
    private val initiator = AgentRole(
        id = "1b47567f-8f7b-444b-aae3-b0c634622d10",
        name = "Iniciador",
        description = "Agentes que coletam informação e desestabilizam a defesa inimiga.",
    )
    private val sentinel = AgentRole(
        id = "5fc02f99-4091-4486-a531-98459a3e95e9",
        name = "Sentinela",
        description = "Agentes que seguram áreas, protegem os flancos e dão suporte defensivo.",
    )
    private val controller = AgentRole(
        id = "4ee40330-ecdd-4f2f-98a8-eb1243428373",
        name = "Controlador",
        description = "Agentes que cortam a visão do inimigo com fumaças e controlam o mapa.",
    )

    val agents: List<Agent> = listOf(
        Agent(
            id = "add6443a-41bd-e414-f6ad-e58d267f4e95",
            name = "Jett",
            description = "Ágil e evasiva, Jett usa o vento a seu favor para entrar no site antes de todos e sair ilesa.",
            developerName = "Wushu",
            role = duelist,
            abilities = listOf(
                Ability(AbilitySlot.GRENADE, "Cloudburst", "Lança uma nuvem que bloqueia a visão por alguns segundos."),
                Ability(AbilitySlot.ABILITY_1, "Updraft", "Impulsiona a Jett para o alto, alcançando posições elevadas."),
                Ability(AbilitySlot.ABILITY_2, "Tailwind", "Dash rápido na direção do movimento, ideal para entrar ou fugir."),
                Ability(AbilitySlot.ULTIMATE, "Blade Storm", "Equipa facas precisas que recarregam a cada abate."),
            ),
            tags = listOf("Mobilidade", "Entrada"),
            colorHex = "#9AD1F1",
        ),
        Agent(
            id = "f94c3b30-42be-e959-889c-5aa313dba261",
            name = "Raze",
            description = "Raze resolve tudo com explosivos: limpa cantos, empurra inimigos e destrói posições fortificadas.",
            developerName = "Clay",
            role = duelist,
            abilities = listOf(
                Ability(AbilitySlot.GRENADE, "Boom Bot", "Robô que persegue inimigos à frente e explode ao alcançá-los."),
                Ability(AbilitySlot.ABILITY_1, "Blast Pack", "Carga explosiva que pode ser detonada para impulsionar a Raze."),
                Ability(AbilitySlot.ABILITY_2, "Paint Shells", "Granada de fragmentação que libera submunições."),
                Ability(AbilitySlot.ULTIMATE, "Showstopper", "Lança-foguetes com dano massivo em área."),
            ),
            tags = null,
            colorHex = "#F8A14A",
        ),
        Agent(
            id = "320b2a48-4d9b-a075-30f1-1f93a9b638fa",
            name = "Sova",
            description = "Rastreador experiente, Sova revela a posição dos inimigos com flechas e drones de reconhecimento.",
            developerName = "Hunter",
            role = initiator,
            abilities = listOf(
                Ability(AbilitySlot.GRENADE, "Owl Drone", "Drone pilotável que marca inimigos atingidos pelo dardo."),
                Ability(AbilitySlot.ABILITY_1, "Shock Bolt", "Flecha elétrica que causa dano ao explodir."),
                Ability(AbilitySlot.ABILITY_2, "Recon Bolt", "Flecha que revela inimigos na linha de visão do sensor."),
                Ability(AbilitySlot.ULTIMATE, "Hunter's Fury", "Três rajadas de energia que atravessam paredes."),
            ),
            tags = listOf("Informação"),
            colorHex = "#7EC7E7",
        ),
        Agent(
            id = "569fdd95-4d10-43ab-ca70-79becc718b46",
            name = "Sage",
            description = "Pilar de apoio do time, Sage cura aliados, bloqueia caminhos e pode trazer companheiros de volta.",
            developerName = "Thorne",
            role = sentinel,
            abilities = listOf(
                Ability(AbilitySlot.GRENADE, "Barrier Orb", "Cria uma parede sólida que pode ser girada antes de posicionar."),
                Ability(AbilitySlot.ABILITY_1, "Slow Orb", "Área que desacelera quem passar por ela."),
                Ability(AbilitySlot.ABILITY_2, "Healing Orb", "Cura um aliado ou a si mesma ao longo do tempo."),
                Ability(AbilitySlot.ULTIMATE, "Resurrection", "Revive um aliado abatido com vida cheia."),
            ),
            tags = listOf("Cura", "Suporte"),
            colorHex = null,
        ),
        Agent(
            id = "1e58de9c-4950-5125-93e9-a0aee9f98746",
            name = "Killjoy",
            description = "Gênia da engenharia, Killjoy defende o bomb com uma rede de torretas e dispositivos automáticos.",
            developerName = null,
            role = sentinel,
            abilities = listOf(
                Ability(AbilitySlot.GRENADE, "Nanoswarm", "Granada oculta que libera nanorrobôs ao ser ativada."),
                Ability(AbilitySlot.ABILITY_1, "Alarmbot", "Robô que persegue inimigos e os deixa vulneráveis."),
                Ability(AbilitySlot.ABILITY_2, "Turret", "Torreta que atira em inimigos dentro do seu cone de visão."),
                Ability(AbilitySlot.ULTIMATE, "Lockdown", "Dispositivo que detém todos os inimigos no raio de alcance."),
            ),
            tags = null,
            colorHex = "#F6D55C",
        ),
        Agent(
            id = "8e253930-4c05-31dd-1b6c-968525494517",
            name = "Omen",
            description = "Um fantasma sem memória, Omen caça nas sombras, cega inimigos e se teleporta pelo mapa.",
            developerName = "Wraith",
            role = controller,
            abilities = listOf(
                Ability(AbilitySlot.GRENADE, "Shrouded Step", "Teleporte curto para um ponto próximo."),
                Ability(AbilitySlot.ABILITY_1, "Paranoia", "Projétil que atravessa paredes e cega os inimigos."),
                Ability(AbilitySlot.ABILITY_2, "Dark Cover", "Fumaça de longo alcance que bloqueia a visão."),
                Ability(AbilitySlot.ULTIMATE, "From the Shadows", "Teleporte para qualquer ponto do mapa."),
            ),
            tags = listOf("Fumaça"),
            colorHex = "#7D6BD6",
        ),
    )

    /** Funções distintas presentes nos mocks, na ordem em que aparecem. */
    val roles: List<AgentRole>
        get() = agents.mapNotNull { it.role }.distinctBy { it.id }

    fun findById(id: String?): Agent? = agents.firstOrNull { it.id == id }
}

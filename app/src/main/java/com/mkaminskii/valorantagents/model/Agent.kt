package com.mkaminskii.valorantagents.model

/**
 * Modelos imutáveis (data class) que representam um agente de Valorant.
 *
 * Campos que nem todo agente possui são nullable (`role`, `developerName`, `tags`
 * e `colorHex`) e a interface decide o que mostrar quando eles estão ausentes.
 */
data class Agent(
    val id: String,
    val name: String,
    val description: String,
    val role: AgentRole?,
    val abilities: List<Ability>,
    val developerName: String? = null,
    val tags: List<String>? = null,
    val colorHex: String? = null,
) {
    /** Primeira letra do nome, exibida no avatar. */
    val initial: String
        get() = name.firstOrNull()?.uppercase() ?: "?"

    /** Habilidades na ordem das teclas do jogo (C, Q, E, X). */
    val orderedAbilities: List<Ability>
        get() = abilities.sortedBy { it.slot.ordinal }
}

data class AgentRole(
    val id: String,
    val name: String,
    val description: String,
)

data class Ability(
    val slot: AbilitySlot,
    val name: String,
    val description: String,
)

/** Slot da habilidade, com a tecla padrão usada no jogo. */
enum class AbilitySlot(val key: String) {
    GRENADE("C"),
    ABILITY_1("Q"),
    ABILITY_2("E"),
    ULTIMATE("X"),
}

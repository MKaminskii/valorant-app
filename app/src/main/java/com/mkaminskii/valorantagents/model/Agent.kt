package com.mkaminskii.valorantagents.model

import androidx.annotation.DrawableRes

data class Agent(
    val id: String,
    val name: String,
    val description: String,
    val role: AgentRole?,
    val abilities: List<Ability>,
    val realName: String? = null,
    val tags: List<String>? = null,
    val colorHex: String? = null,
    @param:DrawableRes val iconRes: Int? = null,
    @param:DrawableRes val portraitRes: Int? = null,
    @param:DrawableRes val backgroundRes: Int? = null,
) {
    val initial: String
        get() = name.firstOrNull()?.uppercase() ?: "?"

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
    @param:DrawableRes val iconRes: Int? = null,
)

enum class AbilitySlot(val key: String) {
    GRENADE("C"),
    ABILITY_1("Q"),
    ABILITY_2("E"),
    ULTIMATE("X"),
}

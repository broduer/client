package com.runesuite.client.game.api

import com.runesuite.client.game.raw.Wrapper
import com.runesuite.client.game.raw.access.XPlayer
import com.runesuite.client.game.raw.access.XPlayerAppearance

class Player(override val accessor: XPlayer) : Actor(accessor) {

    val name get() = accessor.name ?: ""

    val actions: List<String> get() = accessor.actions.toList()

    val combatLevel get() = accessor.combatLevel

    val prayerIcon get() = accessor.prayerIcon

    val skullIcon get() = accessor.skullIcon

    val team get() = accessor.team

    val appearance: PlayerAppearance? get() = accessor.appearance?.let { PlayerAppearance(it) }

    override fun toString(): String {
        return "Player($name)"
    }
}
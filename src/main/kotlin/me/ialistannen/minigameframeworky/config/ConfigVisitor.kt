package me.ialistannen.minigameframeworky.config

import me.ialistannen.minigameframeworky.config.parts.ConfigurationSection
import me.ialistannen.minigameframeworky.config.parts.Group
import me.ialistannen.minigameframeworky.config.parts.Key

/**
 * A visitor for [ConfigurationSection]s.
 */
interface ConfigVisitor {

    /**
     * Called when a [Key] is visited.
     *
     * @param key The [Key]
     */
    fun visit(key: Key)

    /**
     * Called when a [Group] is visited.
     *
     * @param group The [Group]
     */
    fun visit(group: Group)
}
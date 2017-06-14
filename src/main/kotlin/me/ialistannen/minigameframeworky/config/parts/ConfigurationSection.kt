package me.ialistannen.minigameframeworky.config.parts

import me.ialistannen.minigameframeworky.config.ConfigVisitor

/**
 * The base class for config elements
 */
@ConfigDslMarker
interface ConfigurationSection {

    var parent: ConfigurationSection?

    val children: List<ConfigurationSection>
    val keyword: String

    /**
     * @return True if this [ConfigurationSection] has a parent.
     */
    fun hasParent(): Boolean = parent != null

    /**
     * @return The full path to this section. An empty String if none.
     */
    fun getPath(): String

    /**
     * Accepts a Visitor to traverse this tree.
     *
     * @param visitor The visitor
     */
    fun accept(visitor: ConfigVisitor)
}

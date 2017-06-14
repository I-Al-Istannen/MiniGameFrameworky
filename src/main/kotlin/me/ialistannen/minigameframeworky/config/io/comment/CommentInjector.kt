package me.ialistannen.minigameframeworky.config.io.comment

import me.ialistannen.minigameframeworky.config.parts.ConfigurationSection


/**
 * Injects comments into a saved file
 */
@FunctionalInterface
interface CommentInjector {

    /**
     * Injects comments in a file.
     *
     * @param input The input file
     * @param lineMapping The Mappings: <line number it needs to be inserted> -> [ConfigurationSection]
     * @return the contents with comments injected
     */
    fun inject(input: String, lineMapping: Map<Int, ConfigurationSection>): String
}
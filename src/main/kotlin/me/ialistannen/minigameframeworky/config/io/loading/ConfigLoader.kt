package me.ialistannen.minigameframeworky.config.io.loading

import me.ialistannen.minigameframeworky.config.Config

/**
 * Loads a [Config] from a file.
 */
@FunctionalInterface
interface ConfigLoader {

    /**
     * Loads a [Config] from a file.
     *
     * @param input the config string
     * @param name the name of the config
     * @return the loaded [Config]
     */
    fun load(input: String, name: String): Config
}
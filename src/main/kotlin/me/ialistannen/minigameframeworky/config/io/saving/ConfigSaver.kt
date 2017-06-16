package me.ialistannen.minigameframeworky.config.io.saving

import me.ialistannen.minigameframeworky.config.Config

/**
 * A general definition of an object that can save a config
 */
@FunctionalInterface
interface ConfigSaver {

    /**
     * Saves a config to a String.
     *
     * @param config the [Config] to save
     * @param withComments whether to include comments
     * @return the saved config as a String
     */
    fun saveToString(config: Config, withComments: Boolean = true): String
}
package me.ialistannen.minigameframeworky.config.io.comment.mapper

import me.ialistannen.minigameframeworky.config.io.comment.configparser.ConfigParser

/**
 * A Mapper extracting identifiers and their comments from a config
 */
@FunctionalInterface
interface LineMapper {

    /**
     * @param input The String to extract them from
     * @return a list with all items and their line and path
     */
    fun extract(input: String): List<ConfigParser.FoundIdentifier>
}
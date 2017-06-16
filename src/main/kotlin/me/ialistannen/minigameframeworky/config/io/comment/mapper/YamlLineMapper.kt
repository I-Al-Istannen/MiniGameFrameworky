package me.ialistannen.minigameframeworky.config.io.comment.mapper

import me.ialistannen.minigameframeworky.config.io.comment.configparser.ConfigParser
import me.ialistannen.minigameframeworky.config.io.comment.configparser.YamlConfigParser

/**
 * A [LineMapper] for YAML files.
 */
class YamlLineMapper : LineMapper {

    override fun extract(input: String): List<ConfigParser.FoundIdentifier> {
        val comments: MutableList<ConfigParser.FoundIdentifier> = ArrayList()
        val yamlConfigParser = YamlConfigParser()

        yamlConfigParser.onFoundIdentifier = {
            comments.add(it)
        }

        yamlConfigParser.parse(input)

        return comments
    }
}
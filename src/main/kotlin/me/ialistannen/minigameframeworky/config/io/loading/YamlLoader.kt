package me.ialistannen.minigameframeworky.config.io.loading

import me.ialistannen.minigameframeworky.config.Config
import me.ialistannen.minigameframeworky.config.io.comment.configparser.ConfigParser
import me.ialistannen.minigameframeworky.config.io.comment.configparser.ConfigType
import org.bukkit.configuration.MemorySection
import org.bukkit.configuration.file.YamlConfiguration
import java.io.StringReader

/**
 * A YAML [ConfigLoader]
 */
class YamlLoader : ConfigLoader {

    override fun load(input: String, name: String): Config {
        val reader = StringReader(input)
        val yamlConfig = YamlConfiguration.loadConfiguration(reader)

        val parser = ConfigParser.getParser(ConfigType.YAML)
        val commentMap: MutableMap<String, ConfigParser.FoundComment> = HashMap()

        parser.onFoundComment = {
            commentMap.put(it.path, it)
        }
        parser.parse(input)

        val config = Config(name)

        for ((key, value) in yamlConfig.getValues(true)) {
            val comments = if (key in commentMap) commentMap[key]!!.comment else emptyList()
            // bukkit's class for groups
            if (value is MemorySection) {
                config.createGroup(key, comments)
            } else {
                config[key, comments] = value
            }
        }

        return config
    }
}
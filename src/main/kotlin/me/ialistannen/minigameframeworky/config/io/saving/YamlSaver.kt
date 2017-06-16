package me.ialistannen.minigameframeworky.config.io.saving

import me.ialistannen.minigameframeworky.config.Config
import me.ialistannen.minigameframeworky.config.ConfigVisitor
import me.ialistannen.minigameframeworky.config.io.comment.BasicCommentInjector
import me.ialistannen.minigameframeworky.config.io.comment.mapper.YamlLineMapper
import me.ialistannen.minigameframeworky.config.parts.ConfigurationSection
import me.ialistannen.minigameframeworky.config.parts.Group
import me.ialistannen.minigameframeworky.config.parts.Key
import org.bukkit.configuration.file.YamlConfiguration

/**
 * A config saver that saves as YAML
 */
class YamlSaver : ConfigSaver {

    override fun saveToString(config: Config, withComments: Boolean): String {
        return saveToString(config, withComments, true)
    }

    fun saveToString(config: Config,
                     withComments: Boolean,
                     keepEmptyGroups: Boolean = true): String {
        val yamlConfig = YamlConfiguration()

        config.visitPreOrder(YamlVisitor(yamlConfig, keepEmptyGroups))

        var saved = yamlConfig.saveToString()
        if (withComments) {
            val lineMappings = YamlLineMapper().extract(saved).associateBy(
                    { it.line }, { config.get<ConfigurationSection>(it.path)!! }
            )
            saved = BasicCommentInjector("# ").inject(saved, lineMappings)
        }
        return saved
    }

    private class YamlVisitor(val config: YamlConfiguration,
                              val keepEmptyGroups: Boolean) : ConfigVisitor {

        override fun visit(key: Key) {
            config.set(key.getPath(), key.value)
        }

        override fun visit(group: Group) {
            if (!keepEmptyGroups) {
                return
            }
            val path = group.getPath()
            if (path.isNotBlank()) {
                config.createSection(path)
            }
        }

    }
}
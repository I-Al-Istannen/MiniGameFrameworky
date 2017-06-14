package me.ialistannen.minigameframeworky.config.io.saving

import me.ialistannen.minigameframeworky.config.Config
import me.ialistannen.minigameframeworky.config.ConfigVisitor
import me.ialistannen.minigameframeworky.config.parts.Group
import me.ialistannen.minigameframeworky.config.parts.Key
import org.bukkit.configuration.file.YamlConfiguration

/**
 * A config saver that saves as YAML
 */
class YamlSaver : ConfigSaver {

    override fun saveToString(config: Config): String {
        val yamlConfig = YamlConfiguration()

        config.visitPreOrder(YamlVisitor(yamlConfig))

        return yamlConfig.saveToString()
    }

    private class YamlVisitor(val config: YamlConfiguration) : ConfigVisitor {

        override fun visit(key: Key) {
            println("key = [${key}]")
            config.set(key.getPath(), key.value)
        }

        override fun visit(group: Group) {
        }

    }
}
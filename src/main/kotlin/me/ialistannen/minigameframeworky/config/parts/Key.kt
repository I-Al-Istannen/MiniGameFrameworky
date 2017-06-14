package me.ialistannen.minigameframeworky.config.parts

import me.ialistannen.minigameframeworky.config.ConfigVisitor

/**
 * A [ConfigurationSection] denoting a key.
 */
class Key : BaseConfigurationSection() {

    var value: Any? = null

    override fun accept(visitor: ConfigVisitor) {
        visitor.visit(this)
    }

    /**
     * @param value The value of this key
     */
    infix fun value(value: Any?) {
        this.value = value
    }

    override fun toString(): String {
        return "Key(keyword=$keyword, path=${getPath()}, value=$value, comment=$comment)"
    }

}
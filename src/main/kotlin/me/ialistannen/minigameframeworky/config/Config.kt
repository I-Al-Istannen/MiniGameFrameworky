package me.ialistannen.minigameframeworky.config

import me.ialistannen.minigameframeworky.config.parts.ConfigurationSection
import me.ialistannen.minigameframeworky.config.parts.Group
import me.ialistannen.minigameframeworky.config.parts.Key
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

/**
 * A configuration file. Nothing more, nothing less
 *
 * @param name The name of the config
 */
class Config(val name: String) {

    val rootGroup = createRootGroup()

    /**
     * @param name The name of the config
     * @param init Allows for quick initialisation
     */
    constructor(name: String, init: Group.() -> Unit) : this(name) {
        rootGroup.init()
    }

    /**
     * Returns the value at the given path.
     *
     * Returns the [Group] if you ask for it (by using the [Group] generic).
     *
     * Tries to convert numbers for you. May be lossy (e.g. Int to Byte)
     *
     * ***Returns null if there is a type mismatch!***
     *
     * @param path The path to get the value for
     * @return The value at this location or null.
     */
    operator inline fun <reified T> get(path: String): T? {
        val configurationSection: ConfigurationSection = rootGroup[path] ?: return null

        if (T::class == ConfigurationSection::class) {
            return configurationSection as T
        }

        if (configurationSection is Group && T::class == Group::class) {
            return configurationSection as T
        }

        val key = configurationSection as? Key ?: return null

        if (T::class == Key::class) {
            return key as T
        }

        val value = key.value

        if (value is Number && T::class.isSubclassOf(Number::class)) {
            return value.convertTo(T::class) as? T
        }

        return value as? T
    }

    /**
     * Sets an entry. Makes a new [Key].
     *
     * @param path The path to set the value for
     * @param comment A comment.
     * @param value The value
     */
    operator fun set(path: String, comment: String, value: Any?) {
        rootGroup[path, comment] = value
    }

    /**
     * Sets an entry. Makes a new [Key].
     *
     * The comment is an empty String.
     *
     * @param path The path to set the value for
     * @param value The value
     * @see set(String, String, Any?)
     */
    operator fun set(path: String, value: Any?) {
        rootGroup[path, ""] = value
    }

    /**
     * Returns all keys in the config.
     *
     * @param deep Whether it should gather the keys of children too
     * @param includeGroups whether it should skip groups
     * @return all keys in this [Config]
     */
    fun getKeys(deep: Boolean = false, includeGroups: Boolean = false)
            = rootGroup.getKeys(deep, includeGroups)

    /**
     * Returns all values in this config. Skips groups.
     *
     * @param deep Whether it should gather the keys of children too
     * @return all values in this [Config]
     */
    fun getValues(deep: Boolean = false): Map<String, Any?> = rootGroup.getValues(deep)

    /**
     * Visits all nodes in Pre order.
     *
     * @param visitor The [ConfigVisitor] to use
     */
    fun visitPreOrder(visitor: ConfigVisitor) = rootGroup.accept(visitor)

    /**
     * @return The root [Group]
     */
    private fun createRootGroup(): Group {
        val group = Group()
        group name ""
        return group
    }

    /**
     * Converts a [Number] to another class
     *
     * @param classOut The target class
     * @return The converted number or null if not possible
     */
    fun Number.convertTo(classOut: KClass<*>): Number? {
        return when (classOut) {
            Byte::class   -> this.toByte()
            Short::class  -> this.toShort()
            Int::class    -> this.toInt()
            Long::class   -> this.toLong()
            Float::class  -> this.toFloat()
            Double::class -> this.toDouble()
            Number::class -> this
            else          -> null
        }
    }

    operator fun contains(path: String) = path in rootGroup
}
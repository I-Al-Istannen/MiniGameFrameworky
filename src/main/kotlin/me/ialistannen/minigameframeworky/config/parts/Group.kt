package me.ialistannen.minigameframeworky.config.parts

import me.ialistannen.minigameframeworky.config.Config
import me.ialistannen.minigameframeworky.config.ConfigVisitor

/**
 * A [ConfigurationSection] denoting a group. Groups [Key]s together.
 */
class Group : BaseConfigurationSection() {

    /**
     * @param path The path to the entry
     * @return The entry or null
     */
    operator fun get(path: String): ConfigurationSection? {
        if ("." !in path) {
            return getChild(path)
        }
        val parts: List<String> = path.split(".")
        val childKeyword = parts.first()

        if (!hasChild(childKeyword)) {
            return null
        }

        val child = children.filter { it.keyword == childKeyword }.first() as? Group ?: return null

        return child[parts.subList(1, parts.size).joinToString(".")]
    }

    /**
     * Sets an entry. Makes a new [Key].
     *
     * @param path The path to set the value for
     * @param comment An optional comment. Default is an empty String. If you pass nothing,
     * it will preserve any comment that is already present.
     * @param value The value
     */
    operator fun set(path: String, comment: Iterable<String>, value: Any?) {
        val finalComment: MutableList<String> = ArrayList()
        if (comment.count() > 0) {
            finalComment.addAll(comment)
        } else {
            val section = get(path)
            val toAdd = section?.comment ?: emptyList()
            finalComment.addAll(toAdd)
        }
        this[path] = Key().also {
            it value value
            it doc finalComment
        }
    }


    /**
     * Sets an entry to a given [Key].
     *
     * @param path The path to set the value for
     * @param key The [Key] to set
     */
    private operator fun set(path: String, key: BaseConfigurationSection) {
        if ("." !in path) {
            if (hasChild(path)) {
                val child = getChild(path)
                child?.parent = null
                childrenModifiable.remove(child)
            }

            // reset the name
            key.apply { this name path }

            key.parent = this
            childrenModifiable.add(key)

            return
        }
        val parts: List<String> = path.split(".")
        val childKeyword = parts.first()

        val child = getChild(childKeyword)
        val group: Group
        if (child == null || child !is Group) {
            child?.parent = null
            childrenModifiable.remove(child)

            group = Group().also { it name childKeyword }
            childrenModifiable.add(group)
            group.parent = this
        } else {
            group = child
        }
        group[parts.subList(1, parts.size).joinToString(".")] = key
    }

    /**
     * Creates a new group at the given path.
     *
     * @param path the path to create the group at
     * @param comments any comments
     */
    fun createGroup(path: String, comments: Iterable<String>) {
        val group = Group()
        group doc comments
        group name path
        set(path, group)
    }

    /**
     * Checks if this [Group] contains a [ConfigurationSection] with the given keyword.
     *
     * @param keyword The keyword of the element
     */
    operator fun contains(keyword: String): Boolean {
        if ("." !in keyword) {
            return hasChild(keyword)
        }
        val parts: List<String> = keyword.split(".")
        val childKeyword = parts.first()

        if (!hasChild(childKeyword)) {
            return false
        }

        val child = getChild(childKeyword) as? Group ?: return false

        return child.contains(parts.subList(1, parts.size).joinToString("."))
    }

    private fun hasChild(name: String): Boolean {
        return children.any { it.keyword == name }
    }

    private fun getChild(name: String): ConfigurationSection? {
        return children.filter { it.keyword == name }.firstOrNull()
    }

    override fun accept(visitor: ConfigVisitor) {
        visitor.visit(this)
        children.forEach { it.accept(visitor) }
    }

    /**
     * Returns all keys in the config.
     *
     * @param deep whether it should gather the keys of children too
     * @param includeGroups whether it should skip groups
     * @return all keys in this [Config]
     */
    fun getKeys(deep: Boolean = false, includeGroups: Boolean = false): List<String> {
        val list: MutableList<String> = ArrayList()

        list.addAll(children
                .filter { if (includeGroups) true else it is Key }
                .map { it.keyword })

        if (deep) {
            children.filterIsInstance<Group>()
                    .forEach { list.addAll(it.getKeys(true)) }
        }

        return list
    }

    /**
     * Returns all values in this config. Skips groups.
     *
     * @param deep whether it should gather the keys of children too
     * @return all values in this [Config]
     */
    fun getValues(deep: Boolean = false): Map<String, Any?> {
        val map: MutableMap<String, Any?> = HashMap()

        map.putAll(
                children.filterIsInstance<Key>()
                        .associateBy({ it.keyword }, { it.value })
        )

        if (deep) {
            for (child in children) {
                if (child is Key) {
                    map.put(child.keyword, child.value)
                } else if (child is Group) {
                    map.putAll(child.getValues(true))
                }
            }
        }

        return map
    }

    /**
     * Sets and initializes a key as a child of this [Group].
     *
     * @param init The initialisation of the [Key]
     */
    fun key(init: Key.() -> Unit) {
        val key = Key()
        key.init()
        this[key.keyword] = key
    }

    /**
     * Sets and initializes another [Group] as a child of this.
     *
     * @param init The initialisation of the [Group]
     */
    fun group(init: Group.() -> Unit) {
        val group = Group()
        group.init()
        group.parent = this
        childrenModifiable.add(group)
    }

    override fun toString(): String {
        return "Group(keyword=$keyword, path=${getPath()}, children=$children, comment=$comment)"
    }
}
package me.ialistannen.minigameframeworky.config.parts

/**
 * The abstract Base class for [ConfigurationSection]
 */
abstract class BaseConfigurationSection : ConfigurationSection, Documentable {

    override var parent: ConfigurationSection? by kotlin.properties.Delegates.observable(null) {
        _, _: ConfigurationSection?, newValue: ConfigurationSection? ->
        cachedPath = getPathForParent(newValue)
        println("Parent for '$keyword' changed to '$newValue'")
    }

    private var cachedPath: String? = null
    private var keywordImpl: String? = null
    private var commentImpl = ""

    override val children: List<ConfigurationSection> = ArrayList()

    protected val childrenModifiable: MutableList<ConfigurationSection>
        get() = children as MutableList<ConfigurationSection>

    override val keyword: String
        get() = keywordImpl ?: ""

    override fun getPath(): String {
        if (cachedPath == null) {
            cachedPath = getPathForParent(parent)
        }

        return cachedPath ?: ""
    }

    private fun getPathForParent(newValue: ConfigurationSection?): String {
        var path = newValue?.getPath() ?: ""
        if (path.isNotBlank()) {
            path += "."
        }
        path += keyword
        return path
    }

    override val comment: String
        get() = commentImpl

    override fun doc(comment: String) {
        commentImpl = comment
    }

    /**
     * Sets the name of this [ConfigurationSection]
     *
     * @param name The name of this section
     */
    infix fun name(name: String) {
        keywordImpl = name
    }
}
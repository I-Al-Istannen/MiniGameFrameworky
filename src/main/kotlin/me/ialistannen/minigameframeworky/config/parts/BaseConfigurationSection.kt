package me.ialistannen.minigameframeworky.config.parts

/**
 * The abstract Base class for [ConfigurationSection]
 */
abstract class BaseConfigurationSection : ConfigurationSection {

    override var parent: ConfigurationSection? = null
    private var keywordImpl: String? = null
    private val commentImpl: MutableList<String> = ArrayList()

    override val children: List<ConfigurationSection> = ArrayList()

    protected val childrenModifiable: MutableList<ConfigurationSection>
        get() = children as MutableList<ConfigurationSection>

    override val keyword: String
        get() = keywordImpl ?: ""

    override fun getPath(): String {
        var path = parent?.getPath() ?: ""
        if (path.isNotBlank()) {
            path += "."
        }
        path += keyword
        return path

    }

    override val comment: List<String>
        get() = commentImpl

    override fun doc(comments: Iterable<String>) {
        commentImpl.addAll(comments)
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
package me.ialistannen.minigameframeworky.config.parts

/**
 * An element that can be commented
 */
interface Documentable {

    val comment: List<String>

    /**
     * Sets the documentation for this [Documentable].
     *
     * @param comment The comment to display
     */
    infix fun doc(comment: String) = doc(listOf(comment))

    /**
     * Sets the documentation for this [Documentable].
     *
     * @param comments The comments. Each entry is a line
     * @see doc(String)
     */
    infix fun doc(comments: Iterable<String>)
}
package me.ialistannen.minigameframeworky.config.io.comment

import me.ialistannen.minigameframeworky.config.parts.ConfigurationSection


/**
 * A [CommentInjector] injecting it into files given the lines and the comment marker.
 */
class BasicCommentInjector(val commentMarker: String) : CommentInjector {

    override fun inject(input: String, lineMapping: Map<Int, ConfigurationSection>): String {
        val output: MutableList<String> = ArrayList()
        input.lines().withIndex().reversed().forEach {
            output.add(it.value)
            if (it.index in lineMapping) {
                val comment = lineMapping[it.index]?.comment ?: return@forEach
                comment.asReversed().mapTo(output) {
                    commentLine ->
                    getCommentLine(it.value, commentLine)
                }
            }
        }

        return output.asReversed().joinToString(System.lineSeparator())
    }

    private fun getCommentLine(identifierLine: String, comment: String)
            = " ".repeat(identifierLine.getIndent()) + commentMarker + comment
}

/**
 * The indent of a string
 */
fun String.getIndent(): Int {
    return this.takeWhile { it.isWhitespace() }.length
}

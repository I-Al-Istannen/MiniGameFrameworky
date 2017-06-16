package me.ialistannen.minigameframeworky.config.io.comment.configparser

import me.ialistannen.minigameframeworky.config.io.comment.getIndent

/**
 * A YAML [ConfigParser].
 */
class YamlConfigParser : AbstractConfigParser(ConfigType.YAML) {

    private val indentationPerChild: Int = 2

    private var currentIndentationDepth: Int = 0

    override fun parse(input: String) {
        reset(input)

        while (position < string.lastIndex) {
            readComment()
        }
    }

    private fun readComment() {
        val comments: MutableList<String> = ArrayList()

        while (eatIfMatches(LIST_MARKER)) {
            eatLine() // skip the list line
        }

        while (eatIfMatches(COMMENT_MARKER)) {
            val comment = eatLine()
            comments.add(comment.trimStart())
        }

        val readIdentifier = readIdentifier()
        if (readIdentifier.isNotBlank() && comments.isNotEmpty()) {
            onFoundComment(ConfigParser.FoundComment(comments, currentPath, currentLine))
        }
    }

    private fun readIdentifier(): String {
        val read = eatUntil { it == ':' }
        val identifierLine = currentLine
        eatLine() // eat the ":" and the value
        val lastLine = read.lines().last()
        val identifier = lastLine.trim()

        // Was a list item
        if (identifier.startsWith("-")) {
            return ""
        }

        val indent = lastLine.getIndent()

        if (indent > currentIndentationDepth) {
            if (currentPath.isNotBlank()) {
                currentPath += "."
            }
        } else if (indent == currentIndentationDepth) {
            if (indent == 0) {
                currentPath = ""
            } else {
                currentPath = currentPath.substringBeforeLast(".") + "."
            }
        } else if (indent < currentIndentationDepth) {
            val stepsLeft: Int = (currentIndentationDepth - indent) / indentationPerChild
            for (i in 0..stepsLeft) {
                if ("." !in currentPath) {
                    currentPath = ""
                } else {
                    currentPath = currentPath.substringBeforeLast(".")
                }
            }
            if (indent != 0) {
                currentPath += "."
            }
        }
        currentPath += identifier

        currentIndentationDepth = indent

        onFoundIdentifier.invoke(ConfigParser.FoundIdentifier(currentPath, identifierLine))
        return identifier
    }
}

private val COMMENT_MARKER: String = "#"
private val LIST_MARKER: String = "-"
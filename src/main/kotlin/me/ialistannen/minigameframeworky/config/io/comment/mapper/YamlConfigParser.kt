package me.ialistannen.minigameframeworky.config.io.comment.mapper

import me.ialistannen.minigameframeworky.config.io.comment.getIndent

/**
 * A YAML [ConfigParser].
 */
class YamlConfigParser : AbstractConfigParser(ConfigType.YAML) {

    private val indentationPerChild: Int = 2

    private var currentIndentationDeph: Int = 0

    override fun parse(input: String) {
        reset(input)

        val margin = """
            |# I am a long string:and I am never read
            |# Multiline!
            |I_am_cool:
            |I am a very cool guy too:
            |#Nested parent comment
            |nested parent:
            |  # Nested child comment:
            |  nested child:
            |    # deeper comment
            |    deeper_nested child:
            |  # not as deeply comment
            |  not_as_deeply_nested child: lol
            |test_list:
            |- What an item!
            |# Comment for after the list
            |after the list:""".trimMargin()
//        reset(margin)

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

        if (indent > currentIndentationDeph) {
            if (currentPath.isNotBlank()) {
                currentPath += "."
            }
        } else if (indent == currentIndentationDeph) {
            if (indent == 0) {
                currentPath = ""
            } else {
                currentPath = currentPath.substringBeforeLast(".") + "."
            }
        } else if (indent < currentIndentationDeph) {
            val stepsLeft: Int = (currentIndentationDeph - indent) / indentationPerChild
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

        currentIndentationDeph = indent

        onFoundIdentifier.invoke(ConfigParser.FoundIdentifier(currentPath, identifierLine))
        return identifier
    }
}

private val COMMENT_MARKER: String = "#"
private val LIST_MARKER: String = "-"
package me.ialistannen.minigameframeworky.config.io.comment.configparser

/**
 * A skeleton implementation for [ConfigParser]
 */
abstract class AbstractConfigParser(override val configType: ConfigType) : ConfigParser {

    protected var position: Int = 0
    protected var string: String = ""

    protected var currentPath: String = ""
    protected var currentLine: Int = 0

    override var onFoundComment: (ConfigParser.FoundComment) -> Unit = {}
    override var onFoundIdentifier: (ConfigParser.FoundIdentifier) -> Unit = {}

    /**
     * Resets the state.
     *
     * @param input the string to parse
     */
    protected fun reset(input: String) {
        position = 0
        currentPath = ""
        currentLine = 0
        string = input.replace("\r\n", "\n")
    }

    /**
     * Eats until the predicate is true.
     *
     * Does NOT advance beyond the match. [position] will be the index of the match
     *
     * *Will accept the end of the string as a match. This means readUntil may end at the string's
     * last index*
     *
     * @param predicate the predicate to use for matching the char
     * @return the eaten string. Empty String if nothing was eaten.
     */
    protected fun eatUntil(predicate: (Char) -> Boolean): String {
        val startPos = position
        for (i in position..string.lastIndex) {
            if (predicate.invoke(string[i]) || i == string.lastIndex) {
                position = i

                val end = if (i == string.lastIndex) string.length else position
                val substring = string.substring(startPos, end)
                currentLine += substring.count { it == '\n' }

                return substring
            }
        }
        return ""
    }

    /**
     * Eats all whitespace it finds. Includes line breaks.
     */
    protected fun eatWhitespace() {
        eatUntil { !it.isWhitespace() }
    }

    /**
     * Eats the passed string, if it matches.
     *
     * [position] will be the index of the next char after the [toMatch] string.
     *
     * @param toMatch the string to match
     * @param ignoreWhitespace whether to ignore whitespace (calls [eatWhitespace] before)
     * @return true if the string was found, false otherwise.
     */
    protected fun eatIfMatches(toMatch: String, ignoreWhitespace: Boolean = true): Boolean {
        val startPos = position
        val startLine = currentLine

        if (ignoreWhitespace) {
            eatWhitespace()
        }
        if (string.substring(position).startsWith(toMatch)) {
            currentLine += toMatch.count { it == '\n' }
            position += toMatch.length
            return true
        }

        // reset as eatWhitespace moves it!
        position = startPos
        currentLine = startLine
        return false
    }

    /**
     * Eats the current line, until it encounters `\n`.
     *
     * @return the eaten string
     */
    protected fun eatLine(): String {
        val startPos = position

        var eatenString = eatUntil { it == '\n' }

        // Eat if there
        eatIfMatches("\n", ignoreWhitespace = false)

        // No \n found in the string.
        if (startPos == position) {
            position = string.lastIndex
            eatenString = string.substring(startPos)
        }

        return eatenString
    }
}
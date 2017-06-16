package me.ialistannen.minigameframeworky.config.io.comment.mapper

/**
 * Parses a file in a SIMPLE way, having only two actions:
 *
 * * Found comment
 * * Found identifier
 *
 * Both occur with their line number
 */
interface ConfigParser {

    val configType: ConfigType

    /**
     * Called when a comment is found.
     */
    val onFoundComment: (FoundComment) -> Unit

    /**
     * Called when an Identifier is found.
     */
    val onFoundIdentifier: (FoundIdentifier) -> Unit

    /**
     * Parses the input type and calls the handlers when appropiate.
     *
     * @param input the input string to parse.
     */
    fun parse(input: String)

    /**
     * @param comment the comment
     * @param path the path to the identifier
     * @param line the line number it occurred in
     */
    data class FoundComment(val comment: List<String>, val path: String, val line: Int)

    /**
     * @param path the path to the identifier
     * @param line the line number it occurred in
     */
    data class FoundIdentifier(val path: String, val line: Int)
}

/**
 * The type of the config
 */
enum class ConfigType {
    JSON, YAML
}
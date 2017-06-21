package me.ialistannen.minigameframeworky.language

import me.ialistannen.minigameframeworky.config.Config
import java.nio.file.Paths

import java.util.*

typealias x = Set<String>

/**
 * Testing!
 */
fun main(args: Array<String>) {
    val config = Config("messages") {
        key {
            this name "basic_key"
            this value "I am a &5basic&r key. My arguments: {0}, {1, number,$0.00} and {2}. " +
                    "Nested is '[[nested_example]]'"
        }
        key {
            this name "nested_example"
            this value "&c[Super cool plugin]&r "
        }
    }
    val language = Language(Locale.ENGLISH, config)

    println()
    println("==== Translating raw ====")
    println()
    println(language.translate("basic_key", Resolve.NONE, 2, 5, "[[nested_example]]"))

    println()
    println("==== Translating color ====")
    println()
    println(language.translate("basic_key", Resolve.COLOR, 2, 5, "[[nested_example]]"))

    println()
    println("==== Translating placeholder ====")
    println()
    println(language.translate("basic_key", Resolve.PLACEHOLDER, 2, 5, "[[nested_example]]"))

    println()
    println("==== Translating nested ====")
    println()
    println(language.translate("basic_key", Resolve.NESTED, 2, 5, "[[nested_example]]"))

    println()
    println("==== Translating color nested ====")
    println()
    println(language.translate("basic_key", Resolve.COLOR_NESTED, 2, 5, "[[nested_example]]"))

    println()
    println("==== Translating color placeholder ====")
    println()
    println(language.translate("basic_key", Resolve.COLOR_PLACEHOLDER, 2, 5, "[[nested_example]]"))

    println()
    println("==== Translating nested placeholder ====")
    println()
    println(language.translate("basic_key", Resolve.NESTED_PLACEHOLDER, 2, 5, "[[nested_example]]"))

    println()
    println("==== Translating ALL ====")
    println()
    println(language.translate("basic_key", Resolve.ALL, 2, 5, "[[nested_example]]"))

    println()
    println("==== LanguageManager tests ====")
    println()

    println(LanguageManager(Paths.get("/tmp", "test")).languages)
}
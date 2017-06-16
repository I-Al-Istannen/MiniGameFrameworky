package me.ialistannen.minigameframeworky.config

import me.ialistannen.minigameframeworky.config.io.comment.BasicCommentInjector
import me.ialistannen.minigameframeworky.config.io.comment.mapper.YamlConfigParser
import me.ialistannen.minigameframeworky.config.io.comment.mapper.YamlLineMapper
import me.ialistannen.minigameframeworky.config.io.loading.YamlLoader
import me.ialistannen.minigameframeworky.config.io.saving.YamlSaver
import me.ialistannen.minigameframeworky.config.parts.ConfigurationSection
import me.ialistannen.minigameframeworky.config.parts.Group
import me.ialistannen.minigameframeworky.config.parts.Key

fun main(args: Array<String>) {
    val config = Config("my cool one") {
        group {
            this name "myGroup"
            this doc "TL comment"
            key {
                this name "hey"
                this value 32
                this doc "This is a comment"
            }

            group {
                this name "Another one"
                this doc "I am comment"
                this doc listOf("Kinda simple", "Multiline")

                key {
                    this doc "Deep comments are my favourite"
                    this name "deeper nested key"
                    this value "lol"
                }
            }

            group {
                this name "no child"
                this doc "I have no children"
            }
        }
    }

    println("False\t" + ("Hey" in config))
    println()
    println("True\t" + ("myGroup" in config))
    println()
    println("True nested\t" + ("myGroup.hey" in config))
    println("True nested\t" + ("myGroup.Another one" in config))
    println()
    println("==== Getting value ====")
    println()
    println("Number\t" + config.get<Number>("myGroup.hey"))
    println("Number\t" + config.get<Byte>("myGroup.hey"))
    println("Number\t" + config.get<Int>("myGroup.hey"))
    println("Number\t" + config.get<Float>("myGroup.hey"))
    println()
    println("==== Setting value ====")
    println()
    config["myGroup.hey"] = 20
    println("Set value to 20, is: '${config.get<Int>("myGroup.hey")}'")
    println()
    println("==== Listing ====")
    println("Keys shallow         \t${config.getKeys(false)}")
    println("Keys shallow  groups \t${config.getKeys(false, true)}")
    println("Keys deep            \t${config.getKeys(true)}")
    println()
    println("Values shallow       \t${config.getValues(false)}")
    println("Values deep          \t${config.getValues(true)}")
    println()
    println("==== Visiting ====")
    config.visitPreOrder(object : ConfigVisitor {
        override fun visit(key: Key) {
            println("Visited       : $key")
        }

        override fun visit(group: Group) {
            println("Visited group : $group")
        }
    })
    println()
    println("==== Saving ====")
    val configAsString = YamlSaver().saveToString(config, false)
    println("Yaml:\n$configAsString")
    println()
    println("==== Comments ====")
    println()
    val lineMappings: Map<Int, ConfigurationSection> = mapOf(
            0 to config.get<Group>("myGroup")!!,
            1 to config.get<Group>("myGroup.Another one")!!,
            2 to config.get<Key>("myGroup.Another one.deeper nested key")!!,
            3 to config.get<Key>("myGroup.hey")!!
    )
    val withcomments = BasicCommentInjector("# ").inject(
            configAsString, lineMappings)
    println("With comments:\n" + withcomments)
    println()
    println("==== Reading back ====")
    println()
    with(YamlConfigParser()) {
        onFoundComment = { println("> $it") }
        onFoundIdentifier = { println("\t> $it") }
        parse(withcomments)
    }
    println()
    println("==== Extracting Identifiers and adding comments ====")
    println()
    val extractedComments = YamlLineMapper().extract(configAsString)
    extractedComments.forEach { println("Identifier found: > $it") }
    println()
    val lineMappingsDynamic = extractedComments
            .associateBy({ it.line }, { config.get<ConfigurationSection>(it.path)!! })
    val withCommentsDynamic = BasicCommentInjector("# ").inject(
            configAsString, lineMappingsDynamic)
    println("With comments dynamic:\n" + withCommentsDynamic)
    println()
    println("==== Back to config ====")
    println()
    val loadedConfig = YamlLoader().load(withCommentsDynamic, config.name)
    println(loadedConfig)
    println()
    println("==== Re-output read config ====")
    println()
    println(YamlSaver().saveToString(loadedConfig, true))
}

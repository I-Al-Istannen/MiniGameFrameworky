package me.ialistannen.minigameframeworky.config

import me.ialistannen.minigameframeworky.config.io.saving.YamlSaver
import me.ialistannen.minigameframeworky.config.parts.Group
import me.ialistannen.minigameframeworky.config.parts.Key

fun main(args: Array<String>) {
    val config = Config("my cool one") {
        group {
            this name "myGroup"
            key {
                this name "hey"
                this value 32
            }

            group {
                this name "Another one"
                this doc "I am comment"
                this doc listOf("Kinda simple", "Multiline")

                key {
                    this name "deeper nested key"
                    this value "lol"
                }
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
    println("Yaml:\n${YamlSaver().saveToString(config)}")
}

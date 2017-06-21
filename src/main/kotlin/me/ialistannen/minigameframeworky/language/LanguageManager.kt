package me.ialistannen.minigameframeworky.language

import me.ialistannen.minigameframeworky.config.Config
import me.ialistannen.minigameframeworky.util.fileNameWithoutExtension
import java.nio.file.FileVisitResult
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.util.*
import kotlin.collections.HashMap

/**
 * The manager for [Language]s.
 */
class LanguageManager(val folder: Path) {

    private val languagesModifiable: MutableMap<Locale, Language> = HashMap()

    val languages: Map<Locale, Language> = languagesModifiable

    init {
        buildLanguageInstances(findLanguageFiles())
                .associateByTo(languagesModifiable, Language::locale)
    }

    /**
     * Converts the given files to Language files.
     *
     * @param files The [Path]s to read from
     * @return a list with language files
     */
    private fun buildLanguageInstances(files: Iterable<Path>): MutableList<Language> {
        val languages: MutableList<Language> = ArrayList()

        for (file in files) {
            var fileName = file.fileNameWithoutExtension()

            var locale = Locale.ROOT
            if ("_" in fileName) {
                val localeName = fileName.split("_").last()

                locale = Locale.forLanguageTag(localeName)
                fileName = fileName.replace("_" + localeName, "")
            }
            languages.add(Language(locale, Config.loadFromFile(file, fileName)))
        }
        return languages
    }

    /**
     * @return a [List] with all found language file [Path]s.
     */
    private fun findLanguageFiles(): List<Path> {
        val files: MutableList<Path> = ArrayList()

        Files.walkFileTree(folder, emptySet(), 1, object : SimpleFileVisitor<Path?>() {
            override fun visitFile(file: Path?, attrs: BasicFileAttributes?): FileVisitResult {
                file?.let {
                    if (it.toAbsolutePath().toString().endsWith(".lang")) {
                        files.add(it.toAbsolutePath())
                    }
                }
                return FileVisitResult.CONTINUE
            }
        })

        return files
    }

    override fun toString(): String {
        return "LanguageManager(folder=$folder)"
    }

}
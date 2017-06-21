package me.ialistannen.minigameframeworky.util

import java.nio.file.Path

/**
 * Some utils for files
 */

/**
 * @return the name of the file
 */
fun Path.fileName(): String {
    return getName(nameCount - 1).toString()
}

/**
 * @return the name of the file without the extension
 */
fun Path.fileNameWithoutExtension(): String {
    val name = getName(nameCount - 1).toString()
            .replaceAfterLast(".", "")
    return name.substring(0, name.lastIndex)
}

/**
 * @return the extension of the file, if any
 */
fun Path.getExtension(): String {
    var extension = fileName()
    extension = extension.replaceBeforeLast(".", "")
    if (extension.startsWith(".")) {
        extension = extension.substring(1)
    }
    return extension
}
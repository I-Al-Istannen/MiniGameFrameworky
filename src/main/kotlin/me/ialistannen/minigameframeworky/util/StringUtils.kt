package me.ialistannen.minigameframeworky.util

import org.bukkit.ChatColor

/**
 * Some string package-level util functions
 */

/**
 * @param colorChar The color char. Default is '&'
 * @return the colored String
 * @see [ChatColor.translateAlternateColorCodes]
 */
fun String.color(colorChar: Char = '&'): String
        = ChatColor.translateAlternateColorCodes(colorChar, this)
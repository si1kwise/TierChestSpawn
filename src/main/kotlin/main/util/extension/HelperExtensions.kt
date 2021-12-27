package main.util.extension

import business.parser.AbstractConfigParser
import org.bukkit.ChatColor
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.configuration.file.FileConfiguration

/**
 * Get a message from the config with translated alternate color codes
 */
fun FileConfiguration.geMessageFromConfig(string: String): String  {
    val value = this.getConfigurationSection("messages")?.getString(string) ?: ""
    return ChatColor.translateAlternateColorCodes('&', value)
}

/**
 * Get a message from the config with translated alternate color codes
 * and replaced parameter placeholders
 */
fun FileConfiguration.getMessageFromConfigWithParameter(string: String, regex: String, replacementValue: String): String  {
    val value = this.getConfigurationSection("messages")?.getString(string)?.replace(regex, replacementValue) ?: ""
    return ChatColor.translateAlternateColorCodes('&', value)
}

/**
 * Parse a configuration section with the given parser
 */
fun <T> ConfigurationSection.parse(parser: AbstractConfigParser<T>) : T {
    return parser.parse(this)
}
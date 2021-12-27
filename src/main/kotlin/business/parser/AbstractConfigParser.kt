package business.parser

import org.bukkit.configuration.ConfigurationSection

abstract class AbstractConfigParser<T> {

    abstract fun parse(configurationSection: ConfigurationSection): T
}
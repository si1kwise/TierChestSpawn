package main.command.abstractcommand

import data.config.ConfigSection
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import main.util.extension.geMessageFromConfig

abstract class AbstractCommand(open val javaPlugin: JavaPlugin) : CommandExecutor {

    val commandInfo: CommandInfo = javaClass.getDeclaredAnnotation(CommandInfo::class.java)

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if (commandInfo.playerRequired && sender !is Player) return true
        if (commandInfo.permissions.isNotEmpty() && !sender.hasPermission(commandInfo.permissions)) {
            sender.sendMessage(javaPlugin.config.geMessageFromConfig("no-permission"))
            return true
        }

        if(commandInfo.playerRequired) {
            execute(sender as Player, command, args)
        } else {
            execute(sender, command, args)
        }
        return true
    }

    open fun execute(sender: CommandSender, command: Command, args: Array<out String>) { }
    open fun execute(player: Player, command: Command, args: Array<out String>) { }

    fun getConfigurationSection(configSection: ConfigSection) : ConfigurationSection? {
        return javaPlugin.config.getConfigurationSection(configSection.path)
    }
}
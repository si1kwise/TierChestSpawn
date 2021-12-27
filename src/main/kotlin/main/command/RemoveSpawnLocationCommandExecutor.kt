package main.command

import data.config.ConfigSection
import main.command.abstractcommand.AbstractCommand
import main.command.abstractcommand.CommandInfo
import org.bukkit.command.Command
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import main.util.extension.geMessageFromConfig

@CommandInfo(name = "removespawnlocation", permissions = "tcs.removespawnlocation", playerRequired = true)
class RemoveSpawnLocationCommandExecutor(
    override val javaPlugin: JavaPlugin
) : AbstractCommand(javaPlugin) {

    override fun execute(player: Player, command: Command, args: Array<out String>) {
        javaPlugin.reloadConfig()
        val configSectionPath = ConfigSection.CHEST_SPAWN_LOCATION.path
        val configurationSection = javaPlugin.config.getConfigurationSection(configSectionPath)

        val blockPosition = player.location.block
        val xPosition = blockPosition.x
        val yPosition = blockPosition.y
        val zPosition = blockPosition.z
        val key = xPosition.toString() + yPosition.toString() + zPosition.toString()

        configurationSection?.set(key, null)
        javaPlugin.saveConfig()

        player.sendMessage(javaPlugin.config.geMessageFromConfig("chest-spawn-location-removed"))
    }
}
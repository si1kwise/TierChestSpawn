package main.command

import data.config.ConfigSection
import main.command.abstractcommand.AbstractCommand
import main.command.abstractcommand.CommandInfo
import main.util.extension.geMessageFromConfig
import main.util.extension.getMessageFromConfigWithParameter
import org.bukkit.command.Command
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

@CommandInfo(name = "setspawnlocation", permissions = "tcs.setspawnlocation", playerRequired = true)
class SetSpawnLocationCommandExecutor(
    override val javaPlugin: JavaPlugin
) : AbstractCommand(javaPlugin) {

    override fun execute(player: Player, command: Command, args: Array<out String>) {
        if (args.isEmpty()) {
            player.sendMessage(javaPlugin.config.geMessageFromConfig("error-specify-tier"))
        } else {
            val tier = args[0]
            var configurationSection = getConfigurationSection(ConfigSection.CHEST_SPAWN_LOCATION)

            if (configurationSection == null) {
                configurationSection = javaPlugin.config.createSection(ConfigSection.CHEST_SPAWN_LOCATION.path)
            }

            storeChestLocationInConfig(configurationSection, tier, player)

            player.sendMessage(javaPlugin.config.getMessageFromConfigWithParameter(
                "chest-spawn-location-added",
                "{tier}",
                tier))
        }
    }

    private fun storeChestLocationInConfig(configurationSection: ConfigurationSection, tier: String, player: Player) {
        val blockPosition = player.location.block
        val xPosition = blockPosition.x
        val yPosition = blockPosition.y
        val zPosition = blockPosition.z
        val key = xPosition.toString() + yPosition.toString() + zPosition.toString()

        configurationSection.set("$key.world", player.world.name)
        configurationSection.set("$key.tier", tier)
        configurationSection.set("$key.x", xPosition)
        configurationSection.set("$key.y", yPosition)
        configurationSection.set("$key.z", zPosition)
        configurationSection.set("$key.facing", player.facing.name)

        javaPlugin.saveConfig()
    }
}
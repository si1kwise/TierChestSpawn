package business.parser

import data.tierchest.ChestSpawnLocation
import org.bukkit.block.BlockFace
import org.bukkit.configuration.ConfigurationSection

class SpawnChestLocationsConfigParser : AbstractConfigParser<List<ChestSpawnLocation>>() {

    override fun parse(configurationSection: ConfigurationSection): List<ChestSpawnLocation> {
        return configurationSection.getKeys(false).map { key ->
            val world = configurationSection.getString("$key.world").orEmpty()
            val tierValue = configurationSection.getString("$key.tier").orEmpty()
            val xValue = configurationSection.getDouble("$key.x")
            val yValue = configurationSection.getDouble("$key.y")
            val zValue = configurationSection.getDouble("$key.z")
            val facing = configurationSection.getString("$key.facing").orEmpty()

            ChestSpawnLocation(
                world,
                tierValue,
                xValue,
                yValue,
                zValue,
                BlockFace.valueOf(facing))
        }
    }
}
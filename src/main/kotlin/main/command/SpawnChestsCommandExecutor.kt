package main.command

import data.tierchest.item.ChestItem
import data.config.ConfigSection
import business.parser.SpawnChestLocationsConfigParser
import business.parser.TierListConfigParser
import main.command.abstractcommand.AbstractCommand
import main.command.abstractcommand.CommandInfo
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.BlockFace
import org.bukkit.block.Chest
import org.bukkit.block.data.BlockData
import org.bukkit.block.data.Directional
import org.bukkit.command.Command
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import main.util.extension.getMessageFromConfigWithParameter
import main.util.extension.parse

@CommandInfo(name = "spawnchests", permissions = "tcs.spawn", playerRequired = true)
class SpawnChestsCommandExecutor(
    override val javaPlugin: JavaPlugin
) : AbstractCommand(javaPlugin) {

    private val chestLocationsConfigParser = SpawnChestLocationsConfigParser()
    private val tierListConfigParser = TierListConfigParser()

    override fun execute(player: Player, command: Command, args: Array<out String>) {
        // Parse the data from the configuration with the given parser
        val chestLocations = getConfigurationSection(ConfigSection.CHEST_SPAWN_LOCATION)?.parse(chestLocationsConfigParser) ?: emptyList()
        val tierListMapping = getConfigurationSection(ConfigSection.TIER_LIST)?.parse(tierListConfigParser) ?: emptyMap()

        chestLocations.forEach { chestLocation ->
            val world = Bukkit.getWorld(chestLocation.worldName)
            val location = Location(world, chestLocation.x, chestLocation.y, chestLocation.z)
            location.block.type = Material.CHEST

            val block = location.block.state as Chest
            val blockData = block.blockData

            setFacing(blockData, chestLocation.blockFace)
            setChestName(block, chestLocation.tier)
            populateChest(tierListMapping, block, chestLocation.tier)

            block.update()
            location.block.blockData = blockData
        }

        sendMessage(player, chestLocations.size)
    }

    private fun sendMessage(
        player: Player,
        chestLocations: Int) {
        player.sendMessage(
            javaPlugin.config.getMessageFromConfigWithParameter(
                "chests-spawned",
                "{amount}",
                chestLocations.toString()
            )
        )
    }

    // Set the facing of the chest
    private fun setFacing(blockData: BlockData, blockFace: BlockFace) {
        if (blockData is Directional) {
            blockData.facing = blockFace
        }
    }

    // Set the name of the chest
    private fun setChestName(chest: Chest, tier: String) {
        chest.customName = tier
    }

    //Populate a the chest by randomly selecting items from the given tier
    private fun populateChest(tierListMapping: Map<String, List<ChestItem>>, chest: Chest, chestTier: String) {
        val minItems = javaPlugin.config.getInt("chest-min-items")
        val maxItems = javaPlugin.config.getInt("chest-max-items")

        val amountOfItems = (minItems..maxItems).random()
        val tierItems = getTierList(tierListMapping, chestTier)
        val blockInventory = chest.snapshotInventory

        blockInventory.clear()

        for (i in 1..amountOfItems) {
            if (tierItems.isNotEmpty()) {
                val item = getItem(tierItems)
                blockInventory.addItem(item.getItemStack())
                tierItems.remove(item)
            }
        }
    }

    private fun getTierList(tierListMapping: Map<String, List<ChestItem>>, chestTier: String): MutableList<ChestItem> =
        tierListMapping[chestTier].orEmpty().toMutableList()

    //Get a random item out of the list based on their chance
    private fun getItem(items: List<ChestItem>): ChestItem {
        val sum = items.sumOf { it.chance }

        val randomNumber: Int = (0..sum).random()
        var counter = 0
        var i = 0
        while (counter < randomNumber) {
            counter += items[i++].chance
        }

        return items[0.coerceAtLeast(i - 1)]
    }
}
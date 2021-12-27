package data.tierchest

import org.bukkit.block.BlockFace

/**
 * data class to store information about a chest spawn location
 */
data class ChestSpawnLocation(
    val worldName: String,
    val tier: String,
    val x: Double,
    val y: Double,
    val z: Double,
    val blockFace: BlockFace
)



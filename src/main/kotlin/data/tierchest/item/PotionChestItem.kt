package data.tierchest.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.potion.PotionData
import org.bukkit.potion.PotionType

/**
 * data class for a potion
 */
data class PotionChestItem(
    override val material: Material,
    private val potionType: PotionType,
    private val extended: Boolean,
    private val upgraded: Boolean,
    override val chance: Int,
    override val minAmount: Int,
    override val maxAmount: Int
) : ChestItem(material, minAmount, maxAmount, chance) {

    override fun getItemMeta(itemStack: ItemStack): ItemMeta {
        val itemMeta = itemStack.itemMeta as PotionMeta

        itemMeta.basePotionData = PotionData(potionType, extended, upgraded)

        return itemMeta
    }
}

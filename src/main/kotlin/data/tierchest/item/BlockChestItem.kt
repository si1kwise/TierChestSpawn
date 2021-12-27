package data.tierchest.item

import org.bukkit.Material
import org.bukkit.enchantments.Enchantment
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * data class for an item potion
 */
class BlockChestItem(
    override val material: Material,
    override val chance: Int,
    override val minAmount: Int,
    override val maxAmount: Int,
    private val enchantmentToLevelMap: Map<Enchantment, Int>
) : ChestItem(material, minAmount, maxAmount, chance) {

    override fun getItemMeta(itemStack: ItemStack): ItemMeta? {
        val itemMeta = itemStack.itemMeta

        enchantmentToLevelMap.forEach {
            val enchantment = it.key
            if (enchantment.canEnchantItem(itemStack) && itemMeta != null) {
                itemMeta.addEnchant(enchantment, it.value, false)
            }
        }

        return itemMeta
    }
}
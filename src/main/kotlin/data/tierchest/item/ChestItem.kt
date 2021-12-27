package data.tierchest.item

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta

/**
 * abstract class which stores the information to create an item
 */
abstract class ChestItem(
    open val material: Material,
    open val chance: Int,
    open val minAmount: Int,
    open val maxAmount: Int
) {

    abstract fun getItemMeta(itemStack: ItemStack) : ItemMeta?

    fun getItemStack() : ItemStack {
        val itemStack = ItemStack(material, getAmount())

        val itemMeta = getItemMeta(itemStack)
        itemStack.itemMeta = itemMeta

        return itemStack
    }

    private fun getAmount() : Int {
        return (minAmount..maxAmount).random()
    }
}



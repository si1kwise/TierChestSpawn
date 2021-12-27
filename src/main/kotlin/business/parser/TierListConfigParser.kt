package business.parser

import data.tierchest.item.BlockChestItem
import data.tierchest.item.ChestItem
import data.tierchest.item.PotionChestItem
import org.bukkit.Material
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.enchantments.EnchantmentWrapper
import org.bukkit.potion.PotionType

class TierListConfigParser : AbstractConfigParser<Map<String, List<ChestItem>>>() {

    override fun parse(configurationSection: ConfigurationSection): Map<String, List<ChestItem>> {
        return configurationSection.getKeys(false).associateWith { key ->
            val itemConfigurationSection = configurationSection.getConfigurationSection(key)

            if (itemConfigurationSection != null) {
                getItems(itemConfigurationSection)
            } else {
                emptyList()
            }
        }
    }

    private fun getItems(configurationSection: ConfigurationSection): List<ChestItem> {
        return configurationSection.getKeys(false).mapNotNull { key ->
            val material = Material.getMaterial(configurationSection.getString("$key.material").orEmpty())
            val chance = configurationSection.getInt("$key.chance", 1)
            val minAmount = configurationSection.getInt("$key.minAmount", 1)
            val maxAmount = configurationSection.getInt("$key.maxAmount", 1)

            if (material != null) {
                getItem(material, chance, minAmount, maxAmount, configurationSection, key)
            } else {
                null
            }
        }
    }

    private fun getItem(
        material: Material,
        chance: Int,
        minAmount: Int,
        maxAmount: Int,
        configurationSection: ConfigurationSection,
        key: String): ChestItem {
        return when (material) {
            Material.POTION, Material.SPLASH_POTION -> getPotionChestItem(
                material,
                chance,
                minAmount,
                maxAmount,
                configurationSection,
                key)
            else -> getBlockChestItem(material, chance, minAmount, maxAmount, configurationSection, key)
        }
    }

    private fun getPotionChestItem(
        material: Material,
        chance: Int,
        minAmount: Int,
        maxAmount: Int,
        configurationSection: ConfigurationSection,
        key: String): PotionChestItem {
        val potionTypeString: String = configurationSection.getString("$key.potionType") ?: PotionType.UNCRAFTABLE.name
        val potionType = PotionType.valueOf(potionTypeString)
        val potionExtended = configurationSection.getBoolean("$key.potionExtended", false).or(false)
        val potionUpgraded = configurationSection.getBoolean("$key.potionUpgraded", false).or(false)

        return PotionChestItem(
            material,
            potionType,
            potionExtended,
            potionUpgraded,
            chance,
            minAmount,
            maxAmount)
    }

    private fun getBlockChestItem(
        material: Material,
        chance: Int,
        minAmount: Int,
        maxAmount: Int,
        configurationSection: ConfigurationSection,
        key: String): BlockChestItem {
        val enchantmentSection = configurationSection.getConfigurationSection("$key.enchantments")
        val enchantmentToLevelMap = enchantmentSection?.getKeys(false)?.associateBy(
            {
                val enchantmentName = enchantmentSection.getString("$it.enchantment")?.toLowerCase() ?: ""
                EnchantmentWrapper(enchantmentName).enchantment
            },
            { enchantmentSection.getInt("$it.enchantmentLevel") }
        ) ?: emptyMap()

        return BlockChestItem(
            material,
            chance,
            minAmount,
            maxAmount,
            enchantmentToLevelMap)
    }
}
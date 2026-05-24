package top.mc506lw.rebar.ironfurnaces.furnace

import org.bukkit.Material
import org.bukkit.NamespacedKey
import top.mc506lw.rebar.ironfurnaces.IronFurnaceKeys

object FurnaceUpgradeRegistry {

    data class FurnaceUpgradeEntry(
        val fromTier: FurnaceTier,
        val toTier: FurnaceTier,
        val resultBlockKey: NamespacedKey,
        val ingredients: List<Pair<Material, Int>>
    )

    private val upgradeMap = mutableMapOf<NamespacedKey, FurnaceUpgradeEntry>()

    fun registerUpgrade(entry: FurnaceUpgradeEntry) {
        upgradeMap[NamespacedKey("ironfurnaces", "upgrade_${entry.fromTier.name.lowercase()}")] = entry
    }

    fun getUpgradeForTier(tier: FurnaceTier): FurnaceUpgradeEntry? = upgradeMap[NamespacedKey("ironfurnaces", "upgrade_${tier.name.lowercase()}")]

    fun getAllUpgrades(): List<FurnaceUpgradeEntry> = upgradeMap.values.toList()

    fun initialize() {
        registerUpgrade(FurnaceUpgradeEntry(
            fromTier = FurnaceTier.COPPER,
            toTier = FurnaceTier.IRON,
            resultBlockKey = IronFurnaceKeys.IRON_FURNACE,
            ingredients = listOf(Material.IRON_INGOT to 8, Material.IRON_BLOCK to 1)
        ))

        registerUpgrade(FurnaceUpgradeEntry(
            fromTier = FurnaceTier.IRON,
            toTier = FurnaceTier.GOLD,
            resultBlockKey = IronFurnaceKeys.GOLD_FURNACE,
            ingredients = listOf(Material.GOLD_INGOT to 8, Material.GOLD_BLOCK to 1)
        ))

        registerUpgrade(FurnaceUpgradeEntry(
            fromTier = FurnaceTier.GOLD,
            toTier = FurnaceTier.DIAMOND,
            resultBlockKey = IronFurnaceKeys.DIAMOND_FURNACE,
            ingredients = listOf(Material.DIAMOND to 8, Material.DIAMOND_BLOCK to 1)
        ))

        registerUpgrade(FurnaceUpgradeEntry(
            fromTier = FurnaceTier.DIAMOND,
            toTier = FurnaceTier.EMERALD,
            resultBlockKey = IronFurnaceKeys.EMERALD_FURNACE,
            ingredients = listOf(Material.EMERALD to 8, Material.EMERALD_BLOCK to 1)
        ))

        registerUpgrade(FurnaceUpgradeEntry(
            fromTier = FurnaceTier.DIAMOND,
            toTier = FurnaceTier.CRYSTAL,
            resultBlockKey = IronFurnaceKeys.CRYSTAL_FURNACE,
            ingredients = listOf(Material.PRISMARINE_CRYSTALS to 8, Material.PRISMARINE to 1)
        ))

        registerUpgrade(FurnaceUpgradeEntry(
            fromTier = FurnaceTier.EMERALD,
            toTier = FurnaceTier.OBSIDIAN,
            resultBlockKey = IronFurnaceKeys.OBSIDIAN_FURNACE,
            ingredients = listOf(Material.OBSIDIAN to 8, Material.CRYING_OBSIDIAN to 1)
        ))

        registerUpgrade(FurnaceUpgradeEntry(
            fromTier = FurnaceTier.CRYSTAL,
            toTier = FurnaceTier.OBSIDIAN,
            resultBlockKey = IronFurnaceKeys.OBSIDIAN_FURNACE,
            ingredients = listOf(Material.OBSIDIAN to 8, Material.CRYING_OBSIDIAN to 1)
        ))

        registerUpgrade(FurnaceUpgradeEntry(
            fromTier = FurnaceTier.OBSIDIAN,
            toTier = FurnaceTier.NETHERITE,
            resultBlockKey = IronFurnaceKeys.NETHERITE_FURNACE,
            ingredients = listOf(Material.NETHERITE_INGOT to 8, Material.NETHERITE_BLOCK to 1)
        ))
    }
}

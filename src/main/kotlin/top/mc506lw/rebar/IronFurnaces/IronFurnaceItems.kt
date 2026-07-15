package top.mc506lw.rebar.ironfurnaces

import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import top.mc506lw.rebar.ironfurnaces.items.*

object IronFurnaceItems {

    val COPPER_FURNACE_ITEM = createItem(Material.FURNACE, IronFurnaceKeys.COPPER_FURNACE)

    val IRON_FURNACE_ITEM = createItem(Material.FURNACE, IronFurnaceKeys.IRON_FURNACE)

    val GOLD_FURNACE_ITEM = createItem(Material.FURNACE, IronFurnaceKeys.GOLD_FURNACE)

    val DIAMOND_FURNACE_ITEM = createItem(Material.FURNACE, IronFurnaceKeys.DIAMOND_FURNACE)

    val EMERALD_FURNACE_ITEM = createItem(Material.FURNACE, IronFurnaceKeys.EMERALD_FURNACE)

    val CRYSTAL_FURNACE_ITEM = createItem(Material.FURNACE, IronFurnaceKeys.CRYSTAL_FURNACE)

    val OBSIDIAN_FURNACE_ITEM = createItem(Material.FURNACE, IronFurnaceKeys.OBSIDIAN_FURNACE)

    val NETHERITE_FURNACE_ITEM = createItem(Material.FURNACE, IronFurnaceKeys.NETHERITE_FURNACE)

    val RAINBOW_FURNACE_ITEM = createItem(Material.BEACON, IronFurnaceKeys.RAINBOW_FURNACE)

    val RAINBOW_CORE = createItem(Material.NETHER_STAR, IronFurnaceKeys.RAINBOW_CORE)

    val RAINBOW_SHELL = createItem(Material.PRISMARINE_SHARD, IronFurnaceKeys.RAINBOW_SHELL)

    val RAINBOW_COAL = createItem(Material.COAL, IronFurnaceKeys.RAINBOW_COAL)

    val RAINBOW_CONNECTOR = createItem(Material.BLAZE_ROD, IronFurnaceKeys.RAINBOW_CONNECTOR)

    val BLAST_UPGRADE = createItem(Material.BLAST_FURNACE, IronFurnaceKeys.BLAST_UPGRADE)

    val SMOKER_UPGRADE = createItem(Material.SMOKER, IronFurnaceKeys.SMOKER_UPGRADE)

    val SPEED_UPGRADE = createItem(Material.SUGAR, IronFurnaceKeys.SPEED_UPGRADE)

    val FUEL_UPGRADE = createItem(Material.COAL_BLOCK, IronFurnaceKeys.FUEL_UPGRADE)

    val INDUSTRIAL_UPGRADE = createItem(Material.IRON_BLOCK, IronFurnaceKeys.INDUSTRIAL_UPGRADE)

    val GENERATOR_UPGRADE = createItem(Material.REDSTONE_BLOCK, IronFurnaceKeys.GENERATOR_UPGRADE)

    fun initialize() {
        RebarItem.register(CopperFurnaceItem::class.java, COPPER_FURNACE_ITEM, IronFurnaceKeys.COPPER_FURNACE)
        RebarItem.register(IronFurnaceItem::class.java, IRON_FURNACE_ITEM, IronFurnaceKeys.IRON_FURNACE)
        RebarItem.register(GoldFurnaceItem::class.java, GOLD_FURNACE_ITEM, IronFurnaceKeys.GOLD_FURNACE)
        RebarItem.register(DiamondFurnaceItem::class.java, DIAMOND_FURNACE_ITEM, IronFurnaceKeys.DIAMOND_FURNACE)
        RebarItem.register(EmeraldFurnaceItem::class.java, EMERALD_FURNACE_ITEM, IronFurnaceKeys.EMERALD_FURNACE)
        RebarItem.register(CrystalFurnaceItem::class.java, CRYSTAL_FURNACE_ITEM, IronFurnaceKeys.CRYSTAL_FURNACE)
        RebarItem.register(ObsidianFurnaceItem::class.java, OBSIDIAN_FURNACE_ITEM, IronFurnaceKeys.OBSIDIAN_FURNACE)
        RebarItem.register(NetheriteFurnaceItem::class.java, NETHERITE_FURNACE_ITEM, IronFurnaceKeys.NETHERITE_FURNACE)
        RebarItem.register(RainbowFurnaceItem::class.java, RAINBOW_FURNACE_ITEM, IronFurnaceKeys.RAINBOW_FURNACE)

        RebarItem.register(RainbowCoreItem::class.java, RAINBOW_CORE)
        RebarItem.register(RainbowShellItem::class.java, RAINBOW_SHELL)
        RebarItem.register(RainbowCoalItem::class.java, RAINBOW_COAL)
        RebarItem.register(RainbowConnectorItem::class.java, RAINBOW_CONNECTOR)

        RebarItem.register(BlastUpgradeItem::class.java, BLAST_UPGRADE)
        RebarItem.register(SmokerUpgradeItem::class.java, SMOKER_UPGRADE)
        RebarItem.register(SpeedUpgradeItem::class.java, SPEED_UPGRADE)
        RebarItem.register(FuelUpgradeItem::class.java, FUEL_UPGRADE)
        RebarItem.register(IndustrialUpgradeItem::class.java, INDUSTRIAL_UPGRADE)
        RebarItem.register(GeneratorUpgradeItem::class.java, GENERATOR_UPGRADE)
    }

    private fun createItem(material: Material, key: org.bukkit.NamespacedKey): ItemStack =
        ItemStackBuilder.rebar(material, key).build()
}

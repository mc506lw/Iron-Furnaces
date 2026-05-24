package top.mc506lw.rebar.ironfurnaces

import io.github.pylonmc.rebar.item.RebarItem
import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import top.mc506lw.rebar.ironfurnaces.items.*

object IronFurnaceItems {

    val COPPER_FURNACE_ITEM: ItemStack = ItemStackBuilder.rebar(Material.FURNACE, IronFurnaceKeys.COPPER_FURNACE)
        .build()

    val IRON_FURNACE_ITEM: ItemStack = ItemStackBuilder.rebar(Material.FURNACE, IronFurnaceKeys.IRON_FURNACE)
        .build()

    val GOLD_FURNACE_ITEM: ItemStack = ItemStackBuilder.rebar(Material.FURNACE, IronFurnaceKeys.GOLD_FURNACE)
        .build()

    val DIAMOND_FURNACE_ITEM: ItemStack = ItemStackBuilder.rebar(Material.FURNACE, IronFurnaceKeys.DIAMOND_FURNACE)
        .build()

    val EMERALD_FURNACE_ITEM: ItemStack = ItemStackBuilder.rebar(Material.FURNACE, IronFurnaceKeys.EMERALD_FURNACE)
        .build()

    val CRYSTAL_FURNACE_ITEM: ItemStack = ItemStackBuilder.rebar(Material.FURNACE, IronFurnaceKeys.CRYSTAL_FURNACE)
        .build()

    val OBSIDIAN_FURNACE_ITEM: ItemStack = ItemStackBuilder.rebar(Material.FURNACE, IronFurnaceKeys.OBSIDIAN_FURNACE)
        .build()

    val NETHERITE_FURNACE_ITEM: ItemStack = ItemStackBuilder.rebar(Material.FURNACE, IronFurnaceKeys.NETHERITE_FURNACE)
        .build()

    val RAINBOW_FURNACE_ITEM: ItemStack = ItemStackBuilder.rebar(Material.BEACON, IronFurnaceKeys.RAINBOW_FURNACE)
        .build()

    val RAINBOW_CORE: ItemStack = ItemStackBuilder.rebar(Material.NETHER_STAR, IronFurnaceKeys.RAINBOW_CORE)
        .build()

    val RAINBOW_SHELL: ItemStack = ItemStackBuilder.rebar(Material.PRISMARINE_SHARD, IronFurnaceKeys.RAINBOW_SHELL)
        .build()

    val RAINBOW_COAL: ItemStack = ItemStackBuilder.rebar(Material.COAL, IronFurnaceKeys.RAINBOW_COAL)
        .build()

    val RAINBOW_CONNECTOR: ItemStack = ItemStackBuilder.rebar(Material.BLAZE_ROD, IronFurnaceKeys.RAINBOW_CONNECTOR)
        .build()

    val BLAST_UPGRADE: ItemStack = ItemStackBuilder.rebar(Material.BLAST_FURNACE, IronFurnaceKeys.BLAST_UPGRADE)
        .build()

    val SMOKER_UPGRADE: ItemStack = ItemStackBuilder.rebar(Material.SMOKER, IronFurnaceKeys.SMOKER_UPGRADE)
        .build()

    val SPEED_UPGRADE: ItemStack = ItemStackBuilder.rebar(Material.SUGAR, IronFurnaceKeys.SPEED_UPGRADE)
        .build()

    val FUEL_UPGRADE: ItemStack = ItemStackBuilder.rebar(Material.COAL_BLOCK, IronFurnaceKeys.FUEL_UPGRADE)
        .build()

    val INDUSTRIAL_UPGRADE: ItemStack = ItemStackBuilder.rebar(Material.IRON_BLOCK, IronFurnaceKeys.INDUSTRIAL_UPGRADE)
        .build()

    val GENERATOR_UPGRADE: ItemStack = ItemStackBuilder.rebar(Material.REDSTONE_BLOCK, IronFurnaceKeys.GENERATOR_UPGRADE)
        .build()

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
}
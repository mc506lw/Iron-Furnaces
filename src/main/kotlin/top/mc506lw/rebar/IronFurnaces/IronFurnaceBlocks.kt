package top.mc506lw.rebar.ironfurnaces

import io.github.pylonmc.rebar.block.RebarBlock
import org.bukkit.Material
import top.mc506lw.rebar.ironfurnaces.furnace.*
import top.mc506lw.rebar.ironfurnaces.IronFurnaceKeys

object IronFurnaceBlocks {

    fun initialize() {
        RebarBlock.register(IronFurnaceKeys.COPPER_FURNACE, Material.FURNACE, CopperFurnace::class.java)
        RebarBlock.register(IronFurnaceKeys.IRON_FURNACE, Material.FURNACE, IronFurnaceImpl::class.java)
        RebarBlock.register(IronFurnaceKeys.GOLD_FURNACE, Material.FURNACE, GoldFurnace::class.java)
        RebarBlock.register(IronFurnaceKeys.DIAMOND_FURNACE, Material.FURNACE, DiamondFurnace::class.java)
        RebarBlock.register(IronFurnaceKeys.EMERALD_FURNACE, Material.FURNACE, EmeraldFurnace::class.java)
        RebarBlock.register(IronFurnaceKeys.CRYSTAL_FURNACE, Material.FURNACE, CrystalFurnace::class.java)
        RebarBlock.register(IronFurnaceKeys.OBSIDIAN_FURNACE, Material.FURNACE, ObsidianFurnace::class.java)
        RebarBlock.register(IronFurnaceKeys.NETHERITE_FURNACE, Material.FURNACE, NetheriteFurnace::class.java)
        RebarBlock.register(IronFurnaceKeys.RAINBOW_FURNACE, Material.BEACON, RainbowFurnace::class.java)

        FurnaceUpgradeRegistry.initialize()
    }
}
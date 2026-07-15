package top.mc506lw.rebar.ironfurnaces

import io.github.pylonmc.rebar.block.RebarBlock
import org.bukkit.Material
import org.bukkit.NamespacedKey
import top.mc506lw.rebar.ironfurnaces.furnace.CopperFurnace
import top.mc506lw.rebar.ironfurnaces.furnace.CrystalFurnace
import top.mc506lw.rebar.ironfurnaces.furnace.DiamondFurnace
import top.mc506lw.rebar.ironfurnaces.furnace.EmeraldFurnace
import top.mc506lw.rebar.ironfurnaces.furnace.GoldFurnace
import top.mc506lw.rebar.ironfurnaces.furnace.IronFurnaceImpl
import top.mc506lw.rebar.ironfurnaces.furnace.NetheriteFurnace
import top.mc506lw.rebar.ironfurnaces.furnace.ObsidianFurnace
import top.mc506lw.rebar.ironfurnaces.furnace.RainbowFurnace

object IronFurnaceBlocks {
    private data class BlockDefinition(
        val key: NamespacedKey,
        val placementMaterial: Material,
        val implementation: Class<out RebarBlock>
    )

    private val definitions = listOf(
        BlockDefinition(IronFurnaceKeys.COPPER_FURNACE, Material.FURNACE, CopperFurnace::class.java),
        BlockDefinition(IronFurnaceKeys.IRON_FURNACE, Material.FURNACE, IronFurnaceImpl::class.java),
        BlockDefinition(IronFurnaceKeys.GOLD_FURNACE, Material.FURNACE, GoldFurnace::class.java),
        BlockDefinition(IronFurnaceKeys.DIAMOND_FURNACE, Material.FURNACE, DiamondFurnace::class.java),
        BlockDefinition(IronFurnaceKeys.EMERALD_FURNACE, Material.FURNACE, EmeraldFurnace::class.java),
        BlockDefinition(IronFurnaceKeys.CRYSTAL_FURNACE, Material.FURNACE, CrystalFurnace::class.java),
        BlockDefinition(IronFurnaceKeys.OBSIDIAN_FURNACE, Material.FURNACE, ObsidianFurnace::class.java),
        BlockDefinition(IronFurnaceKeys.NETHERITE_FURNACE, Material.FURNACE, NetheriteFurnace::class.java),
        BlockDefinition(IronFurnaceKeys.RAINBOW_FURNACE, Material.BEACON, RainbowFurnace::class.java)
    )

    fun initialize() {
        for (definition in definitions) {
            RebarBlock.register(definition.key, definition.placementMaterial, definition.implementation)
        }
    }
}

package top.mc506lw.rebar.ironfurnaces.furnace

import io.github.pylonmc.rebar.block.context.BlockCreateContext
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.persistence.PersistentDataContainer

class CopperFurnace(block: Block, context: BlockCreateContext) : AbstractIronFurnace(
    block,
    context,
    FurnaceTier.COPPER,
    Material.COPPER_BLOCK,
    Material.FURNACE
) {
    constructor(block: Block, pdc: PersistentDataContainer) : this(
        block,
        BlockCreateContext.Default(block)
    )
}

class IronFurnaceImpl(block: Block, context: BlockCreateContext) : AbstractIronFurnace(
    block,
    context,
    FurnaceTier.IRON,
    Material.IRON_BLOCK,
    Material.FURNACE
) {
    constructor(block: Block, pdc: PersistentDataContainer) : this(
        block,
        BlockCreateContext.Default(block)
    )
}

class GoldFurnace(block: Block, context: BlockCreateContext) : AbstractIronFurnace(
    block,
    context,
    FurnaceTier.GOLD,
    Material.GOLD_BLOCK,
    Material.FURNACE
) {
    constructor(block: Block, pdc: PersistentDataContainer) : this(
        block,
        BlockCreateContext.Default(block)
    )
}

class DiamondFurnace(block: Block, context: BlockCreateContext) : AbstractIronFurnace(
    block,
    context,
    FurnaceTier.DIAMOND,
    Material.DIAMOND_BLOCK,
    Material.FURNACE
) {
    constructor(block: Block, pdc: PersistentDataContainer) : this(
        block,
        BlockCreateContext.Default(block)
    )
}

class EmeraldFurnace(block: Block, context: BlockCreateContext) : AbstractIronFurnace(
    block,
    context,
    FurnaceTier.EMERALD,
    Material.EMERALD_BLOCK,
    Material.FURNACE
) {
    constructor(block: Block, pdc: PersistentDataContainer) : this(
        block,
        BlockCreateContext.Default(block)
    )
}

class ObsidianFurnace(block: Block, context: BlockCreateContext) : AbstractIronFurnace(
    block,
    context,
    FurnaceTier.OBSIDIAN,
    Material.OBSIDIAN,
    Material.FURNACE
) {
    constructor(block: Block, pdc: PersistentDataContainer) : this(
        block,
        BlockCreateContext.Default(block)
    )
}

class NetheriteFurnace(block: Block, context: BlockCreateContext) : AbstractIronFurnace(
    block,
    context,
    FurnaceTier.NETHERITE,
    Material.NETHERITE_BLOCK,
    Material.FURNACE
) {
    constructor(block: Block, pdc: PersistentDataContainer) : this(
        block,
        BlockCreateContext.Default(block)
    )
}

package top.mc506lw.rebar.ironfurnaces.furnace

import io.github.pylonmc.rebar.block.context.BlockCreateContext
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.persistence.PersistentDataContainer

class CopperFurnace : AbstractIronFurnace {
    constructor(block: Block, context: BlockCreateContext) :
        super(block, context, FurnaceTier.COPPER, Material.COPPER_BLOCK)

    constructor(block: Block, pdc: PersistentDataContainer) :
        super(block, pdc, FurnaceTier.COPPER, Material.COPPER_BLOCK)
}

class IronFurnaceImpl : AbstractIronFurnace {
    constructor(block: Block, context: BlockCreateContext) :
        super(block, context, FurnaceTier.IRON, Material.IRON_BLOCK)

    constructor(block: Block, pdc: PersistentDataContainer) :
        super(block, pdc, FurnaceTier.IRON, Material.IRON_BLOCK)
}

class GoldFurnace : AbstractIronFurnace {
    constructor(block: Block, context: BlockCreateContext) :
        super(block, context, FurnaceTier.GOLD, Material.GOLD_BLOCK)

    constructor(block: Block, pdc: PersistentDataContainer) :
        super(block, pdc, FurnaceTier.GOLD, Material.GOLD_BLOCK)
}

class DiamondFurnace : AbstractIronFurnace {
    constructor(block: Block, context: BlockCreateContext) :
        super(block, context, FurnaceTier.DIAMOND, Material.DIAMOND_BLOCK)

    constructor(block: Block, pdc: PersistentDataContainer) :
        super(block, pdc, FurnaceTier.DIAMOND, Material.DIAMOND_BLOCK)
}

class EmeraldFurnace : AbstractIronFurnace {
    constructor(block: Block, context: BlockCreateContext) :
        super(block, context, FurnaceTier.EMERALD, Material.EMERALD_BLOCK)

    constructor(block: Block, pdc: PersistentDataContainer) :
        super(block, pdc, FurnaceTier.EMERALD, Material.EMERALD_BLOCK)
}

class ObsidianFurnace : AbstractIronFurnace {
    constructor(block: Block, context: BlockCreateContext) :
        super(block, context, FurnaceTier.OBSIDIAN, Material.OBSIDIAN)

    constructor(block: Block, pdc: PersistentDataContainer) :
        super(block, pdc, FurnaceTier.OBSIDIAN, Material.OBSIDIAN)
}

class NetheriteFurnace : AbstractIronFurnace {
    constructor(block: Block, context: BlockCreateContext) :
        super(block, context, FurnaceTier.NETHERITE, Material.NETHERITE_BLOCK)

    constructor(block: Block, pdc: PersistentDataContainer) :
        super(block, pdc, FurnaceTier.NETHERITE, Material.NETHERITE_BLOCK)
}

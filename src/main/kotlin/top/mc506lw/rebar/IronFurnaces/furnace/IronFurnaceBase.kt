package top.mc506lw.rebar.ironfurnaces.furnace

import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.persistence.PersistentDataContainer

abstract class IronFurnaceBase : RebarBlock {

    protected val furnaceTier: FurnaceTier
    protected val baseMaterial: Material
    private val guiMaterial: Material

    protected constructor(
        block: Block,
        context: BlockCreateContext,
        furnaceTier: FurnaceTier,
        baseMaterial: Material,
        guiMaterial: Material = Material.FURNACE
    ) : super(block, context) {
        this.furnaceTier = furnaceTier
        this.baseMaterial = baseMaterial
        this.guiMaterial = guiMaterial
    }

    protected constructor(
        block: Block,
        pdc: PersistentDataContainer,
        furnaceTier: FurnaceTier,
        baseMaterial: Material,
        guiMaterial: Material = Material.FURNACE
    ) : super(block, pdc) {
        this.furnaceTier = furnaceTier
        this.baseMaterial = baseMaterial
        this.guiMaterial = guiMaterial
    }

    fun getGuiMaterial(): Material = guiMaterial
}
package top.mc506lw.rebar.ironfurnaces.furnace

import io.github.pylonmc.rebar.block.context.BlockCreateContext
import io.github.pylonmc.rebar.entity.display.ItemDisplayBuilder
import io.github.pylonmc.rebar.entity.display.transform.TransformBuilder
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.entity.Display
import org.bukkit.entity.ItemDisplay
import org.bukkit.persistence.PersistentDataContainer

class CrystalFurnace(block: Block, context: BlockCreateContext) : AbstractIronFurnace(
    block,
    context,
    FurnaceTier.CRYSTAL,
    Material.LIGHT_BLUE_STAINED_GLASS,
    Material.FURNACE
) {
    constructor(block: Block, pdc: PersistentDataContainer) : this(
        block,
        BlockCreateContext.Default(null, block)
    )

    companion object {
        private const val BORDER_ENTITY_PREFIX = "edge_"
        private val BORDER_MATERIAL = Material.LIGHT_BLUE_STAINED_GLASS

        private const val EDGE_THICKNESS = 0.06
        private const val EDGE_OFFSET = 0.5 - EDGE_THICKNESS / 2.0
        private const val THIN_SCALE = EDGE_THICKNESS * 2.0
        private const val EDGE_LENGTH = 0.875
        private const val LENGTH_SCALE = EDGE_LENGTH * 2.0

        private data class EdgeDef(
            val name: String,
            val tx: Double, val ty: Double, val tz: Double,
            val sx: Double, val sy: Double, val sz: Double
        )

        private val EDGES = listOf(
            EdgeDef("v_nw", -EDGE_OFFSET, 0.0, -EDGE_OFFSET, THIN_SCALE, LENGTH_SCALE, THIN_SCALE),
            EdgeDef("v_ne",  EDGE_OFFSET, 0.0, -EDGE_OFFSET, THIN_SCALE, LENGTH_SCALE, THIN_SCALE),
            EdgeDef("v_se",  EDGE_OFFSET, 0.0,  EDGE_OFFSET, THIN_SCALE, LENGTH_SCALE, THIN_SCALE),
            EdgeDef("v_sw", -EDGE_OFFSET, 0.0,  EDGE_OFFSET, THIN_SCALE, LENGTH_SCALE, THIN_SCALE),

            EdgeDef("h_top_n",  0.0,  EDGE_OFFSET, -EDGE_OFFSET, LENGTH_SCALE, THIN_SCALE, THIN_SCALE),
            EdgeDef("h_top_e",  EDGE_OFFSET,  EDGE_OFFSET, 0.0, THIN_SCALE, THIN_SCALE, LENGTH_SCALE),
            EdgeDef("h_top_s",  0.0,  EDGE_OFFSET,  EDGE_OFFSET, LENGTH_SCALE, THIN_SCALE, THIN_SCALE),
            EdgeDef("h_top_w", -EDGE_OFFSET,  EDGE_OFFSET, 0.0, THIN_SCALE, THIN_SCALE, LENGTH_SCALE),

            EdgeDef("h_bot_n",  0.0, -EDGE_OFFSET, -EDGE_OFFSET, LENGTH_SCALE, THIN_SCALE, THIN_SCALE),
            EdgeDef("h_bot_e",  EDGE_OFFSET, -EDGE_OFFSET, 0.0, THIN_SCALE, THIN_SCALE, LENGTH_SCALE),
            EdgeDef("h_bot_s",  0.0, -EDGE_OFFSET,  EDGE_OFFSET, LENGTH_SCALE, THIN_SCALE, THIN_SCALE),
            EdgeDef("h_bot_w", -EDGE_OFFSET, -EDGE_OFFSET, 0.0, THIN_SCALE, THIN_SCALE, LENGTH_SCALE)
        )
    }

    override fun setupBlockType() {
        block.type = Material.STRUCTURE_VOID
    }

    override fun postInitialise() {
        super.postInitialise()
        createBorder()
    }

    override fun postLoad() {
        super.postLoad()
        createBorder()
    }

    private fun createBorder() {
        val centerLoc = block.location.toCenterLocation()

        for (edge in EDGES) {
            val entityName = "${BORDER_ENTITY_PREFIX}${edge.name}"
            if (heldEntities.containsKey(entityName)) continue

            val transformation = TransformBuilder()
                .translate(edge.tx, edge.ty, edge.tz)
                .scale(edge.sx, edge.sy, edge.sz)
                .buildForItemDisplay()

            val display = ItemDisplayBuilder()
                .material(BORDER_MATERIAL)
                .itemDisplayTransform(ItemDisplay.ItemDisplayTransform.FIXED)
                .brightness(Display.Brightness(15, 15))
                .transformation(transformation)
                .build(centerLoc)

            addEntity(entityName, display)
        }
    }
}

package top.mc506lw.rebar.ironfurnaces.furnace

import io.github.pylonmc.rebar.entity.display.BlockDisplayBuilder
import io.github.pylonmc.rebar.entity.display.transform.TransformBuilder
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Display

open class FurnaceDisplayRenderer(
    protected val block: Block,
    private val getFacing: () -> BlockFace,
    private val getEntity: (String) -> java.util.UUID?,
    private val addEntityFunc: (String, Display) -> Unit
) {
    companion object {
        private const val FRONT_FACE_ENTITY_NAME = "furnace_front"
        private const val FRONT_OFFSET = 0.499

        fun getFrontFaceTransformation(facing: BlockFace): TransformBuilder {
            return when (facing) {
                BlockFace.NORTH -> TransformBuilder()
                    .translate(0.0, 0.0, -FRONT_OFFSET)
                    .scale(1.0, 1.0, 0.01)
                BlockFace.SOUTH -> TransformBuilder()
                    .translate(0.0, 0.0, FRONT_OFFSET)
                    .scale(1.0, 1.0, 0.01)
                BlockFace.EAST -> TransformBuilder()
                    .translate(FRONT_OFFSET, 0.0, 0.0)
                    .scale(0.01, 1.0, 1.0)
                BlockFace.WEST -> TransformBuilder()
                    .translate(-FRONT_OFFSET, 0.0, 0.0)
                    .scale(0.01, 1.0, 1.0)
                else -> TransformBuilder()
                    .translate(0.0, 0.0, -FRONT_OFFSET)
                    .scale(1.0, 1.0, 0.01)
            }
        }
    }

    private var wasBurning = false
    private var currentBlockType: String = "furnace"

    open fun createFrontFace(blockType: String = "furnace") {
        if (getEntity(FRONT_FACE_ENTITY_NAME) != null) return

        currentBlockType = blockType
        val centerLoc = block.location.toCenterLocation()
        val facing = getFacing()
        val facingStr = facing.name.lowercase()
        val blockData = org.bukkit.Bukkit.createBlockData("minecraft:$blockType[lit=false,facing=$facingStr]")

        val display = BlockDisplayBuilder()
            .blockData(blockData)
            .brightness(Display.Brightness(15, 15))
            .transformation(getFrontFaceTransformation(facing))
            .build(centerLoc)

        addEntityFunc(FRONT_FACE_ENTITY_NAME, display)
    }

    open fun updateFrontFace() {
        val entityUuid = getEntity(FRONT_FACE_ENTITY_NAME) ?: return
        val entity = block.world.getEntity(entityUuid) as? BlockDisplay ?: return

        entity.setTransformationMatrix(getFrontFaceTransformation(getFacing()).buildForBlockDisplay())
    }

    open fun updateBurningState(isBurning: Boolean, blockType: String? = null) {
        val entityUuid = getEntity(FRONT_FACE_ENTITY_NAME) ?: return
        val entity = block.world.getEntity(entityUuid) as? BlockDisplay ?: return

        if (isBurning == wasBurning && (blockType == null || blockType == currentBlockType)) return
        wasBurning = isBurning

        val effectiveBlockType = blockType ?: currentBlockType
        if (blockType != null && blockType != currentBlockType) {
            currentBlockType = blockType
        }

        val litStr = if (isBurning) "true" else "false"
        val facingStr = getFacing().name.lowercase()
        entity.block = org.bukkit.Bukkit.createBlockData("minecraft:$effectiveBlockType[lit=$litStr,facing=$facingStr]")
    }

    open fun updateDisplayType(blockType: String) {
        if (blockType == currentBlockType) return

        currentBlockType = blockType
        val entityUuid = getEntity(FRONT_FACE_ENTITY_NAME) ?: return
        val entity = block.world.getEntity(entityUuid) as? BlockDisplay ?: return

        val litStr = if (wasBurning) "true" else "false"
        val facingStr = getFacing().name.lowercase()
        entity.block = org.bukkit.Bukkit.createBlockData("minecraft:$blockType[lit=$litStr,facing=$facingStr]")
    }

    open fun resetState() {
        wasBurning = false
        currentBlockType = "furnace"
    }

    open fun getCurrentBlockType(): String = currentBlockType

    open fun createAdditionalEffects() {}
}

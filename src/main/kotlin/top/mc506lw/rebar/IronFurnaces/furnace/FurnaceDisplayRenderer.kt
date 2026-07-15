package top.mc506lw.rebar.ironfurnaces.furnace

import io.github.pylonmc.rebar.entity.display.BlockDisplayBuilder
import io.github.pylonmc.rebar.entity.display.transform.TransformBuilder
import org.bukkit.Bukkit
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Display
import java.util.UUID
import java.util.Locale

open class FurnaceDisplayRenderer(
    protected val block: Block,
    private val getFacing: () -> BlockFace,
    private val getEntity: (String) -> UUID?,
    private val addEntity: (String, Display) -> Unit,
    private val removeEntity: (String) -> Unit = {}
) {
    companion object {
        private const val FRONT_FACE_ENTITY_NAME = "furnace_front"
        private const val FRONT_OFFSET = 0.499

        fun getFrontFaceTransformation(facing: BlockFace): TransformBuilder = when (facing) {
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

        private fun horizontalFacing(facing: BlockFace): BlockFace = when (facing) {
            BlockFace.NORTH, BlockFace.SOUTH, BlockFace.EAST, BlockFace.WEST -> facing
            else -> BlockFace.NORTH
        }
    }

    private var wasBurning = false
    private var currentBlockType = "furnace"

    open fun createFrontFace(blockType: String = "furnace") = ensureFrontFace(blockType)

    open fun ensureFrontFace(blockType: String = "furnace") {
        currentBlockType = blockType
        if (resolveFrontFace() != null) return
        if (getEntity(FRONT_FACE_ENTITY_NAME) != null) removeEntity(FRONT_FACE_ENTITY_NAME)

        val facing = horizontalFacing(getFacing())
        val display = BlockDisplayBuilder()
            .blockData(createBlockData(blockType, false, facing))
            .brightness(Display.Brightness(15, 15))
            .transformation(getFrontFaceTransformation(facing))
            .build(block.location.toCenterLocation())

        addEntity(FRONT_FACE_ENTITY_NAME, display)
    }

    open fun updateFrontFace() {
        val facing = horizontalFacing(getFacing())
        val entity = resolveFrontFace() ?: return
        entity.setTransformationMatrix(getFrontFaceTransformation(facing).buildForBlockDisplay())
    }

    open fun updateBurningState(isBurning: Boolean, blockType: String? = null) {
        val effectiveBlockType = blockType ?: currentBlockType
        if (isBurning == wasBurning && effectiveBlockType == currentBlockType) return

        wasBurning = isBurning
        currentBlockType = effectiveBlockType
        val entity = resolveFrontFace() ?: return
        entity.block = createBlockData(effectiveBlockType, isBurning, horizontalFacing(getFacing()))
    }

    open fun updateDisplayType(blockType: String) {
        if (blockType == currentBlockType) return
        currentBlockType = blockType

        val entity = resolveFrontFace() ?: return
        entity.block = createBlockData(blockType, wasBurning, horizontalFacing(getFacing()))
    }

    open fun refreshState(isBurning: Boolean, blockType: String) {
        wasBurning = isBurning
        currentBlockType = blockType
        val entity = resolveFrontFace() ?: return
        entity.block = createBlockData(blockType, isBurning, horizontalFacing(getFacing()))
    }

    open fun resetState() {
        wasBurning = false
        currentBlockType = "furnace"
    }

    open fun getCurrentBlockType(): String = currentBlockType

    open fun createAdditionalEffects() = Unit

    private fun resolveFrontFace(): BlockDisplay? {
        val uuid = getEntity(FRONT_FACE_ENTITY_NAME) ?: return null
        return block.world.getEntity(uuid) as? BlockDisplay
    }

    private fun createBlockData(blockType: String, lit: Boolean, facing: BlockFace) =
        Bukkit.createBlockData(
            "minecraft:$blockType[lit=$lit,facing=${facing.name.lowercase(Locale.ROOT)}]"
        )
}

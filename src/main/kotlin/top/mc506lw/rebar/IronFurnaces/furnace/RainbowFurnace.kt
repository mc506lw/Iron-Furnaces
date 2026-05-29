package top.mc506lw.rebar.ironfurnaces.furnace

import io.github.pylonmc.rebar.block.context.BlockCreateContext
import io.github.pylonmc.rebar.util.MachineUpdateReason
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import org.bukkit.persistence.PersistentDataType
import xyz.xenondevs.invui.inventory.VirtualInventory
import java.util.*

class RainbowFurnace(block: Block, context: BlockCreateContext) : AbstractIronFurnace(
    block,
    context,
    FurnaceTier.RAINBOW,
    Material.WHITE_WOOL,
    Material.FURNACE
) {
    constructor(block: Block, pdc: PersistentDataContainer) : this(
        block,
        BlockCreateContext.Default(null, block)
    ) {
        colorIndex = pdc.get(COLOR_INDEX_KEY, PersistentDataType.INTEGER) ?: 0
        colorTickCounter = pdc.get(COLOR_TICK_KEY, PersistentDataType.INTEGER) ?: 0
    }

    override var outputInv = VirtualInventory(1)

    init {
        speedMultiplier = 3.2
    }

    override val rainbowSpeedLabel: String
        get() = "10x-640x"

    private var colorIndex = 0
    private var colorTickCounter = 0

    private val woolColors = arrayOf(
        Material.WHITE_WOOL,
        Material.ORANGE_WOOL,
        Material.MAGENTA_WOOL,
        Material.LIGHT_BLUE_WOOL,
        Material.YELLOW_WOOL,
        Material.LIME_WOOL,
        Material.PINK_WOOL,
        Material.GRAY_WOOL,
        Material.LIGHT_GRAY_WOOL,
        Material.CYAN_WOOL,
        Material.PURPLE_WOOL,
        Material.BLUE_WOOL,
        Material.BROWN_WOOL,
        Material.GREEN_WOOL,
        Material.RED_WOOL,
        Material.BLACK_WOOL
    )

    private val dustColors = arrayOf(
        Color.WHITE,
        Color.ORANGE,
        Color.FUCHSIA,
        Color.AQUA,
        Color.YELLOW,
        Color.LIME,
        Color.fromRGB(255, 175, 175),
        Color.GRAY,
        Color.SILVER,
        Color.TEAL,
        Color.PURPLE,
        Color.BLUE,
        Color.MAROON,
        Color.GREEN,
        Color.RED,
        Color.BLACK
    )

    private val random = Random()

    companion object {
        private val LOGGER = java.util.logging.Logger.getLogger("RainbowFurnace")
        private val COLOR_INDEX_KEY = org.bukkit.NamespacedKey("ironfurnaces", "rainbow_color_index")
        private val COLOR_TICK_KEY = org.bukkit.NamespacedKey("ironfurnaces", "rainbow_color_tick")
    }

    override fun write(pdc: PersistentDataContainer) {
        super.write(pdc)
        pdc.set(COLOR_INDEX_KEY, PersistentDataType.INTEGER, colorIndex)
        pdc.set(COLOR_TICK_KEY, PersistentDataType.INTEGER, colorTickCounter)
    }

    override fun setupBlockType() {
        block.type = woolColors[colorIndex % woolColors.size]
    }

    override fun tick() {
        super.tick()

        colorTickCounter++
        if (colorTickCounter >= 10) {
            colorTickCounter = 0
            colorIndex = (colorIndex + 1) % woolColors.size
            block.type = woolColors[colorIndex]
        }
    }

    override fun spawnSmokeParticle() {
        val loc = block.location.toCenterLocation()
        val world = loc.world ?: return

        val color = dustColors[colorIndex % dustColors.size]
        world.spawnParticle(
            Particle.DUST,
            loc.add(0.0, 1.0, 0.0),
            3 + random.nextInt(3),
            0.3, 0.5, 0.3,
            Particle.DustOptions(color, 1.5f)
        )
    }

    override fun onRecipeFinished(recipe: io.github.pylonmc.rebar.recipe.vanilla.FurnaceRecipeWrapper) {
        var input = inputInv.getItem(0)
        if (input == null || input.isEmpty()) {
            super.onRecipeFinished(recipe)
            return
        }

        val result = recipe.recipe.result.clone()
        var processed = 0

        while (input != null && !input.isEmpty() && fuelRemaining > 0 && outputInv.canHold(result)) {
            inputInv.setItem(MachineUpdateReason(), 0, input.subtract())
            outputInv.addItem(null, result.clone())
            processed++
            fuelRemaining -= tickInterval
            input = inputInv.getItem(0)
        }

        if (fuelRemaining <= 0) {
            fuelRemaining = 0
        }

        tryStartSmelting()

        if (!isProcessingRecipe) {
            recipeProgressItem.setItem(io.github.pylonmc.rebar.util.gui.GuiItems.background())
        }
    }
}

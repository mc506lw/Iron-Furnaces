package top.mc506lw.rebar.ironfurnaces.furnace

import io.github.pylonmc.rebar.block.RebarBlock
import io.github.pylonmc.rebar.block.base.*
import io.github.pylonmc.rebar.block.context.BlockBreakContext
import io.github.pylonmc.rebar.block.context.BlockCreateContext
import io.github.pylonmc.rebar.entity.display.BlockDisplayBuilder
import io.github.pylonmc.rebar.entity.display.transform.TransformBuilder
import io.github.pylonmc.rebar.event.api.annotation.MultiHandler
import io.github.pylonmc.rebar.i18n.RebarArgument
import io.github.pylonmc.rebar.logistics.LogisticGroupType
import io.github.pylonmc.rebar.recipe.vanilla.FurnaceRecipeWrapper
import io.github.pylonmc.rebar.recipe.vanilla.FurnaceRecipeType
import io.github.pylonmc.rebar.util.MachineUpdateReason
import io.github.pylonmc.rebar.util.gui.GuiItems
import io.github.pylonmc.rebar.util.gui.ProgressItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.Particle
import org.bukkit.block.Block
import org.bukkit.block.BlockFace
import org.bukkit.entity.Display
import org.bukkit.entity.BlockDisplay
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.block.BlockCookEvent
import org.bukkit.event.inventory.ClickType
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataContainer
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.inventory.VirtualInventory
import xyz.xenondevs.invui.window.Window

abstract class AbstractIronFurnace(
    block: Block,
    context: BlockCreateContext,
    val furnaceTier: FurnaceTier,
    val baseMaterial: Material,
    private val guiMaterial: Material = Material.FURNACE
) : RebarBlock(block, context),
    RebarGuiBlock,
    RebarVirtualInventoryBlock,
    RebarDirectionalBlock,
    RebarTickingBlock,
    RebarLogisticBlock,
    RebarFurnace,
    RebarRecipeProcessor<FurnaceRecipeWrapper>,
    RebarEntityHolderBlock,
    RebarBreakHandler {

    companion object {
        private val LOGGER = java.util.logging.Logger.getLogger("IronFurnace")
    }

    constructor(
        block: Block,
        pdc: PersistentDataContainer,
        furnaceTier: FurnaceTier,
        baseMaterial: Material,
        guiMaterial: Material = Material.FURNACE
    ) : this(block, BlockCreateContext.Default(block), furnaceTier, baseMaterial, guiMaterial)

    override var disableBlockTextureEntity = true

    protected open val inputInv = VirtualInventory(1)
    protected open val outputInv = VirtualInventory(1)
    protected open val fuelInv = VirtualInventory(1)

    protected val upgradeRedSlot = VirtualInventory(1)
    protected val upgradeGreenSlot = VirtualInventory(1)
    protected val upgradeBlueSlot = VirtualInventory(1)

    protected val fuelSystem: FurnaceFuelSystem by lazy {
        FurnaceFuelSystem(block, furnaceTier, fuelInv, inputInv)
    }

    protected val displayRenderer: FurnaceDisplayRenderer by lazy {
        createDisplayRenderer()
    }

    protected val guiFactory: FurnaceGuiFactory by lazy {
        FurnaceGuiFactory(
            this,
            guiMaterial,
            inputInv,
            outputInv,
            fuelInv,
            fuelSystem,
            recipeProgressItem,
            upgradeRedSlot,
            upgradeGreenSlot,
            upgradeBlueSlot
        )
    }

    protected open fun createDisplayRenderer(): FurnaceDisplayRenderer {
        return FurnaceDisplayRenderer(
            block = block,
            getFacing = { facing },
            getEntity = { name -> heldEntities[name] },
            addEntityFunc = { name, entity -> addEntity(name, entity) }
        )
    }

    init {
        setRecipeType(FurnaceRecipeType)
        recipeProgressItem = ProgressItem(GuiItems.background(), true)

        if (context is BlockCreateContext.PlayerPlace) {
            facing = context.facing
        }
    }

    override val tickInterval: Int = (furnaceTier.smeltTimePerItem / 10).coerceAtLeast(1)

    init {
        setTickInterval(tickInterval)
    }

    var fuelEfficiency: Double
        get() = fuelSystem.fuelEfficiency
        set(value) { fuelSystem.fuelEfficiency = value }

    open var speedMultiplier: Double = 1.0
        protected set

    var currentFuelTime: Int
        get() = fuelSystem.currentFuelTime
        set(value) { fuelSystem.currentFuelTime = value }

    var fuelRemaining: Int
        get() = fuelSystem.fuelRemaining
        set(value) { fuelSystem.fuelRemaining = value }

    private val speedLabel: String
        get() {
            val effectiveTime = (furnaceTier.smeltTimePerItem / speedMultiplier).toInt().coerceAtLeast(1)
            val mult = 200.0 / effectiveTime
            return "%.1fx".format(mult).replace(".0x", "x")
        }

    protected open val rainbowSpeedLabel: String?
        get() = null

    override fun postInitialise() {
        createLogisticGroup("input", LogisticGroupType.INPUT, inputInv)
        createLogisticGroup("output", LogisticGroupType.OUTPUT, outputInv)

        outputInv.addPreUpdateHandler { event ->
            if (!event.isRemove && event.updateReason is xyz.xenondevs.invui.inventory.event.PlayerUpdateReason) {
                event.isCancelled = true
            }
        }
        outputInv.addPostUpdateHandler { tryStartSmelting() }
        inputInv.addPostUpdateHandler { event ->
            if (event.updateReason !is MachineUpdateReason) {
                tryStartSmelting()
            }
        }

        setupBlockType()
        displayRenderer.createFrontFace()
        displayRenderer.updateBurningState(fuelSystem.isBurning)
    }

    override fun postLoad() {
        super.postLoad()
        displayRenderer.resetState()
        setupBlockType()
        displayRenderer.updateFrontFace()
        displayRenderer.updateBurningState(fuelSystem.isBurning)
        if (!isProcessingRecipe) {
            recipeProgressItem.setItem(GuiItems.background())
        }
    }

    protected open fun setupBlockType() {
        block.type = baseMaterial
    }

    override fun postBreak(context: BlockBreakContext) {
        tryRemoveAllEntities()
    }

    override fun tick() {
        fuelSystem.consumeFuel()

        fuelSystem.updateFuelState(tickInterval)

        fuelSystem.consumeFuel()

        if (isProcessingRecipe) {
            val input = inputInv.getItem(0)
            if (input == null || input.isEmpty()) {
                stopRecipe()
                recipeProgressItem.setItem(GuiItems.background())
            }
        }

        tryStartSmelting()

        if (isProcessingRecipe && fuelSystem.isBurning) {
            progressRecipe(tickInterval)
            spawnSmokeParticle()
        }

        displayRenderer.updateBurningState(fuelSystem.isBurning)
    }

    open fun tryStartSmelting() {
        if (isProcessingRecipe) return

        val stack = inputInv.getItem(0)
        if (stack == null || stack.isEmpty()) return

        if (fuelRemaining <= 0) {
            LOGGER.fine("[${furnaceTier.name}] ${block.location} 无法烧制: fuelRemaining=$fuelRemaining")
            return
        }

        if (lastRecipe != null && tryStartSmelting(lastRecipe!!, stack)) return

        for (recipe in FurnaceRecipeType.recipes) {
            if (tryStartSmelting(recipe, stack)) break
        }
    }

    private fun tryStartSmelting(recipe: FurnaceRecipeWrapper, stack: ItemStack): Boolean {
        if (!recipe.isInput(stack)) return false
        if (!outputInv.canHold(recipe.recipe.result)) return false

        val actualTime = (furnaceTier.smeltTimePerItem / speedMultiplier).toInt().coerceAtLeast(1)
        val displaySpeed = rainbowSpeedLabel ?: speedLabel

        recipeProgressItem.setItem(
            io.github.pylonmc.rebar.item.builder.ItemStackBuilder.of(ItemStack(Material.FLINT_AND_STEEL))
                .name(Component.translatable("ironfurnaces.gui.smelt_progress.name"))
                .lore(Component.translatable("ironfurnaces.gui.smelt_progress.lore", RebarArgument.of("speed", displaySpeed)))
                .build()
        )
        startRecipe(recipe, actualTime)

        return true
    }

    override fun onRecipeFinished(recipe: FurnaceRecipeWrapper) {
        val input = inputInv.getItem(0)
        if (input != null && !input.isEmpty()) {
            inputInv.setItem(MachineUpdateReason(), 0, input.subtract())
            outputInv.addItem(null, recipe.recipe.result.clone())
        }

        tryStartSmelting()

        if (!isProcessingRecipe) {
            recipeProgressItem.setItem(GuiItems.background())
        }
    }

    @MultiHandler(priorities = [EventPriority.LOWEST])
    override fun onEndSmelting(event: BlockCookEvent, priority: EventPriority) {
        event.isCancelled = true
    }

    override fun createGui(): Gui = guiFactory.createMainGui()

    override fun getWaila(player: Player): io.github.pylonmc.rebar.waila.WailaDisplay? {
        return io.github.pylonmc.rebar.waila.WailaDisplay(defaultWailaTranslationKey)
    }

    override fun getVirtualInventories(): Map<String, VirtualInventory> = mapOf(
        "input" to inputInv,
        "output" to outputInv,
        "fuel" to fuelInv,
        "upgrade_red" to upgradeRedSlot,
        "upgrade_green" to upgradeGreenSlot,
        "upgrade_blue" to upgradeBlueSlot
    )

    protected open fun spawnSmokeParticle() {
        block.location.toCenterLocation().world?.spawnParticle(
            Particle.SMOKE,
            block.location.toCenterLocation().add(0.0, 0.8, 0.0),
            1,
            0.1, 0.2, 0.1,
            0.01
        )
    }
}

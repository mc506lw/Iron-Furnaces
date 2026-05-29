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
    furnaceTier: FurnaceTier,
    baseMaterial: Material,
    guiMaterial: Material = Material.FURNACE
) : IronFurnaceBase(block, context, furnaceTier, baseMaterial, guiMaterial),
    RebarVirtualInventoryBlock,
    RebarDirectionalBlock,
    RebarTickingBlock,
    RebarLogisticBlock,
    RebarFurnace,
    RebarRecipeProcessor<FurnaceRecipeWrapper>,
    RebarEntityHolderBlock,
    RebarBreakHandler,
    RebarInteractBlock {

    companion object {
        private val LOGGER = java.util.logging.Logger.getLogger("IronFurnace")
    }

    constructor(
        block: Block,
        pdc: PersistentDataContainer,
        furnaceTier: FurnaceTier,
        baseMaterial: Material,
        guiMaterial: Material = Material.FURNACE
    ) : this(block, BlockCreateContext.Default(null, block), furnaceTier, baseMaterial, guiMaterial)

    override var disableBlockTextureEntity = true

    protected open val inputInv = VirtualInventory(1)
    protected open var outputInv = VirtualInventory(1)
    protected open val fuelInv = VirtualInventory(1)

    protected val upgradeRedSlot = VirtualInventory(1)
    protected val upgradeGreenSlot = VirtualInventory(1)
    protected val upgradeBlueSlot = VirtualInventory(1)

    protected val upgradeManager: UpgradeEffectManager by lazy {
        UpgradeEffectManager(upgradeRedSlot, upgradeGreenSlot, upgradeBlueSlot)
    }

    protected val energySystem: FurnaceEnergySystem by lazy {
        FurnaceEnergySystem(block)
    }

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
        recipeProgressItem = InvertedProgressItem(GuiItems.background())

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

    var speedMultiplier: Double
        get() {
            val effects = upgradeManager.calculateEffects()
            return effects.speedMultiplier
        }
        protected set(value) {}

    var currentFuelTime: Int
        get() = fuelSystem.currentFuelTime
        set(value) { fuelSystem.currentFuelTime = value }

    var fuelRemaining: Int
        get() = fuelSystem.fuelRemaining
        set(value) { fuelSystem.fuelRemaining = value }

    private val speedLabel: String
        get() {
            val effects = upgradeManager.calculateEffects()
            val effectiveTime = (furnaceTier.smeltTimePerItem * effects.smeltTimeModifier).toInt().coerceAtLeast(1)
            val mult = 200.0 / effectiveTime
            return "%.1fx".format(mult).replace(".0x", "x")
        }

    protected open val rainbowSpeedLabel: String?
        get() = null

    override fun postInitialise() {
        createLogisticGroup("input", LogisticGroupType.INPUT, inputInv)
        createLogisticGroup("output", LogisticGroupType.INPUT, outputInv)

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

        setupUpgradeSlots()
        setupBlockType()

        val displayType = upgradeManager.getDisplayBlockType()
        displayRenderer.createFrontFace(displayType)
        displayRenderer.updateBurningState(fuelSystem.isBurning, displayType)
    }

    private fun setupUpgradeSlots() {
        val updateHandler: () -> Unit = {
            onUpgradesChanged()
        }

        upgradeRedSlot.addPreUpdateHandler { event -> validateUpgradePlacement(event, 0) }
        upgradeRedSlot.addPostUpdateHandler { updateHandler() }

        upgradeGreenSlot.addPreUpdateHandler { event -> validateUpgradePlacement(event, 1) }
        upgradeGreenSlot.addPostUpdateHandler { updateHandler() }

        upgradeBlueSlot.addPreUpdateHandler { event -> validateUpgradePlacement(event, 2) }
        upgradeBlueSlot.addPostUpdateHandler { updateHandler() }
    }

    protected open fun onUpgradesChanged() {
        upgradeManager.invalidateCache()
        applyUpgradeEffects()
        updateDisplayForUpgrades()
    }

    protected open fun applyUpgradeEffects() {
        val effects = upgradeManager.calculateEffects()

        fuelSystem.fuelConsumptionRate = effects.fuelConsumptionRate
        fuelSystem.fuelEfficiency = effects.fuelEfficiencyBonus
        fuelSystem.speedMultiplier = effects.speedMultiplier

        if (effects.outputSlots != outputInv.size) {
            outputInv = VirtualInventory(effects.outputSlots)
        }
    }

    protected open fun updateDisplayForUpgrades() {
        val displayType = upgradeManager.getDisplayBlockType()
        displayRenderer.updateDisplayType(displayType)
    }

    override fun postLoad() {
        super.postLoad()
        displayRenderer.resetState()
        setupBlockType()

        val displayType = upgradeManager.getDisplayBlockType()
        displayRenderer.updateFrontFace()
        displayRenderer.updateBurningState(fuelSystem.isBurning, displayType)

        if (!isProcessingRecipe) {
            recipeProgressItem.setItem(GuiItems.background())
        }

        applyUpgradeEffects()
    }

    protected open fun setupBlockType() {
        block.type = baseMaterial
    }

    override fun postBreak(context: BlockBreakContext) {
        tryRemoveAllEntities()
    }

    override fun tick() {
        val effects = upgradeManager.calculateEffects()

        when (effects.mode) {
            FurnaceMode.GENERATOR_ONLY,
            FurnaceMode.GENERATOR_BLAST,
            FurnaceMode.GENERATOR_SMOKER -> handleGeneratorTick(effects)
            else -> handleNormalTick(effects)
        }

        displayRenderer.updateBurningState(fuelSystem.isBurning, upgradeManager.getDisplayBlockType())
    }

    private fun handleNormalTick(effects: UpgradeEffects) {
        if (!effects.canSmelt) return

        if (isProcessingRecipe) {
            val input = inputInv.getItem(0)
            if (input == null || input.isEmpty()) {
                stopRecipe()
                recipeProgressItem.setItem(GuiItems.background())
            }
        }

        tryStartSmelting()

        if (!effects.usesEnergy) {
            if (fuelSystem.isBurning) {
                fuelSystem.updateFuelState(tickInterval)
            } else if (isProcessingRecipe) {
                fuelSystem.consumeFuel()
                fuelSystem.updateFuelState(tickInterval)
            } else {
                fuelSystem.updateFuelState(tickInterval)
            }
        }

        if (isProcessingRecipe && (effects.usesEnergy || fuelSystem.isBurning)) {
            progressRecipe(tickInterval)
            spawnSmokeParticle()
        } else if (!fuelSystem.isBurning && isProcessingRecipe) {
            stopRecipe()
            recipeProgressItem.setItem(GuiItems.background())
        }
    }

    private fun handleIndustrialEnergyTick(effects: UpgradeEffects) {
        val energyRequired = 10.0 * tickInterval
        if (energySystem.consumeEnergy(energyRequired)) {
            if (fuelRemaining <= 0) {
                fuelRemaining = Int.MAX_VALUE / 2
                currentFuelTime = fuelRemaining
            }
        } else {
            if (fuelRemaining > 0) {
                fuelRemaining = 0
            }
        }
    }

    private fun handleGeneratorTick(effects: UpgradeEffects) {
        if (fuelSystem.isBurning) {
            val heatAmount = tickInterval.toDouble()
            val energyProduced = energySystem.convertHeatToEnergy(
                heatAmount,
                effects.generatorPowerMultiplier
            )

            val speedModifier = effects.generatorSpeedMultiplier
            val adjustedTickInterval = (tickInterval * speedModifier).toInt().coerceAtLeast(1)

            for (i in 0 until adjustedTickInterval) {
                fuelSystem.updateFuelState(1)
            }
        } else {
            fuelSystem.consumeFuel()
            fuelSystem.updateFuelState(tickInterval)
        }
    }

    open fun tryStartSmelting() {
        if (isProcessingRecipe) return

        val effects = upgradeManager.calculateEffects()
        if (!effects.canSmelt) return

        val stack = inputInv.getItem(0)
        if (stack == null || stack.isEmpty()) return

        if (lastRecipe != null && tryStartSmelting(lastRecipe!!, stack, effects)) return

        for (recipe in FurnaceRecipeType.recipes) {
            if (tryStartSmelting(recipe, stack, effects)) break
        }
    }

    private fun tryStartSmelting(recipe: FurnaceRecipeWrapper, stack: ItemStack, effects: UpgradeEffects): Boolean {
        if (!recipe.isInput(stack)) return false

        if (!upgradeManager.isRecipeCompatible(RecipeDetector.detectRecipeCompatibility(stack))) {
            LOGGER.fine("[${furnaceTier.name}] ${block.location} 配方不兼容当前模式: ${stack.type}")
            return false
        }

        if (!outputInv.canHold(recipe.recipe.result)) return false

        val actualTime = (furnaceTier.smeltTimePerItem * effects.smeltTimeModifier).toInt().coerceAtLeast(1)
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

    fun createGui(): Gui = guiFactory.createMainGui()

    fun getGuiTitle(): Component = Component.translatable("ironfurnaces.item.${furnaceTier.name.lowercase()}_furnace.name")

    override fun onInteract(event: org.bukkit.event.player.PlayerInteractEvent, priority: org.bukkit.event.EventPriority) {
        if (priority != org.bukkit.event.EventPriority.NORMAL) return
        if (!event.action.isRightClick || event.hand != org.bukkit.inventory.EquipmentSlot.HAND) return

        event.isCancelled = true

        Window.builder()
            .setUpperGui(createGui())
            .setTitle(getGuiTitle())
            .setViewer(event.player)
            .build()
            .open()
    }

    override fun getWaila(player: Player): io.github.pylonmc.rebar.waila.WailaDisplay? {
        return io.github.pylonmc.rebar.waila.WailaDisplay(defaultWailaTranslationKey)
    }

    private fun validateUpgradePlacement(
        event: xyz.xenondevs.invui.inventory.event.ItemPreUpdateEvent,
        slotIndex: Int
    ) {
        val newItem = event.newItem
        if (newItem == null || newItem.isEmpty()) return

        val upgradeType = UpgradeEffectManager.getUpgradeType(newItem)

        if (upgradeType == null) {
            event.isCancelled = true
            return
        }

        if (!UpgradeEffectManager.canPlaceInSlot(upgradeType, slotIndex)) {
            event.isCancelled = true
            return
        }

        if (UpgradeEffectManager.isDuplicateUpgrade(upgradeType, upgradeRedSlot, upgradeGreenSlot, upgradeBlueSlot)) {
            event.isCancelled = true
            return
        }
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

class InvertedProgressItem(
    item: xyz.xenondevs.invui.item.Item
) : ProgressItem(item, true) {

    @Suppress("UnstableApiUsage")
    override fun getItemProvider(viewer: org.bukkit.entity.Player): xyz.xenondevs.invui.item.ItemProvider {
        if (totalTime == null) {
            return super.getItemProvider(viewer)
        }

        val provider = super.getItemProvider(viewer)
        val stack = provider.get()

        val builder = io.github.pylonmc.rebar.item.builder.ItemStackBuilder.of(stack.clone())

        val currentDamage = builder.get(io.papermc.paper.datacomponent.DataComponentTypes.DAMAGE) ?: 0
        val maxDamage = builder.get(io.papermc.paper.datacomponent.DataComponentTypes.MAX_DAMAGE) ?: 1000

        var invertedDamage = (maxDamage - currentDamage).coerceIn(0, maxDamage)

        if (invertedDamage <= 1) {
            invertedDamage = maxDamage
        }

        builder.set(io.papermc.paper.datacomponent.DataComponentTypes.DAMAGE, invertedDamage)

        return builder
    }
}

package top.mc506lw.rebar.ironfurnaces.furnace

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.inventory.VirtualInventory
import top.mc506lw.rebar.ironfurnaces.upgrades.UpgradeType

enum class FurnaceMode {
    NORMAL,
    BLAST,
    SMOKER,
    GENERATOR_ONLY,
    INDUSTRIAL,
    GENERATOR_BLAST,
    GENERATOR_SMOKER
}

data class UpgradeEffects(
    val mode: FurnaceMode = FurnaceMode.NORMAL,
    val speedMultiplier: Double = 1.0,
    val fuelConsumptionRate: Double = 1.0,
    val fuelEfficiencyBonus: Double = 1.0,
    val smeltTimeModifier: Double = 1.0,
    val outputSlots: Int = 1,
    val usesEnergy: Boolean = false,
    val generatorPowerMultiplier: Double = 1.0,
    val generatorSpeedMultiplier: Double = 1.0,
    val canSmelt: Boolean = true
)

class UpgradeEffectManager(
    private val upgradeRedSlot: VirtualInventory,
    private val upgradeGreenSlot: VirtualInventory,
    private val upgradeBlueSlot: VirtualInventory
) {
    companion object {
        private val LOGGER = java.util.logging.Logger.getLogger("IronFurnace-Upgrades")

        private val UPGRADE_ITEMS = mapOf(
            Material.BLAST_FURNACE to UpgradeType.BLAST,
            Material.SMOKER to UpgradeType.SMOKER,
            Material.SUGAR to UpgradeType.SPEED,
            Material.COAL_BLOCK to UpgradeType.FUEL,
            Material.IRON_BLOCK to UpgradeType.INDUSTRIAL,
            Material.REDSTONE_BLOCK to UpgradeType.GENERATOR
        )

        private val RED_SLOT_TYPES = setOf(UpgradeType.BLAST, UpgradeType.SMOKER)
        private val GREEN_SLOT_TYPES = setOf(UpgradeType.SPEED, UpgradeType.FUEL)
        private val BLUE_SLOT_TYPES = setOf(UpgradeType.INDUSTRIAL, UpgradeType.GENERATOR)

        fun getUpgradeType(stack: ItemStack?): UpgradeType? {
            if (stack == null || stack.isEmpty()) return null
            return UPGRADE_ITEMS[stack.type]
        }

        fun canPlaceInSlot(upgradeType: UpgradeType, slotIndex: Int): Boolean {
            return when (slotIndex) {
                0 -> upgradeType in RED_SLOT_TYPES
                1 -> upgradeType in GREEN_SLOT_TYPES
                2 -> upgradeType in BLUE_SLOT_TYPES
                else -> false
            }
        }

        fun isDuplicateUpgrade(
            upgradeType: UpgradeType,
            redSlot: VirtualInventory,
            greenSlot: VirtualInventory,
            blueSlot: VirtualInventory
        ): Boolean {
            val slots = listOf(redSlot, greenSlot, blueSlot)
            var count = 0

            slots.forEach { slot ->
                val stack = slot.getItem(0)
                if (getUpgradeType(stack) == upgradeType) {
                    count++
                    if (count >= 1) return true
                }
            }

            return false
        }
    }

    private var cachedEffects: UpgradeEffects? = null
    private var lastSlotHash: Int = 0

    fun getInstalledUpgrades(): Set<UpgradeType> {
        val upgrades = mutableSetOf<UpgradeType>()

        listOf(upgradeRedSlot, upgradeGreenSlot, upgradeBlueSlot).forEach { slot ->
            val stack = slot.getItem(0)
            getUpgradeType(stack)?.let { upgrades.add(it) }
        }

        return upgrades
    }

    fun calculateEffects(): UpgradeEffects {
        val currentHash = calculateSlotHash()
        if (currentHash == lastSlotHash && cachedEffects != null) {
            return cachedEffects!!
        }

        lastSlotHash = currentHash
        val upgrades = getInstalledUpgrades()
        cachedEffects = computeUpgradeCombination(upgrades)

        return cachedEffects!!
    }

    private fun calculateSlotHash(): Int {
        var hash = 0
        listOf(upgradeRedSlot, upgradeGreenSlot, upgradeBlueSlot).forEach { slot ->
            val stack = slot.getItem(0)
            hash = hash * 31 + (stack?.type?.ordinal ?: -1)
        }
        return hash
    }

    private fun computeUpgradeCombination(upgrades: Set<UpgradeType>): UpgradeEffects {
        val hasBlast = UpgradeType.BLAST in upgrades
        val hasSmoker = UpgradeType.SMOKER in upgrades
        val hasSpeed = UpgradeType.SPEED in upgrades
        val hasFuel = UpgradeType.FUEL in upgrades
        val hasIndustrial = UpgradeType.INDUSTRIAL in upgrades
        val hasGenerator = UpgradeType.GENERATOR in upgrades

        when {
            hasGenerator && hasBlast -> return createGeneratorBlastEffects(hasSpeed, hasFuel, hasIndustrial)
            hasGenerator && hasSmoker -> return createGeneratorSmokerEffects(hasSpeed, hasFuel, hasIndustrial)
            hasGenerator -> return createGeneratorOnlyEffects(hasSpeed, hasFuel, hasIndustrial)
            hasIndustrial -> return createIndustrialEffects(hasBlast, hasSmoker, hasSpeed, hasFuel, hasGenerator)
            hasBlast -> return createBlastEffects(hasSpeed, hasFuel)
            hasSmoker -> return createSmokerEffects(hasSpeed, hasFuel)
            else -> return createNormalEffects(hasSpeed, hasFuel)
        }
    }

    private fun createNormalEffects(hasSpeed: Boolean, hasFuel: Boolean): UpgradeEffects {
        var speedMult = 1.0
        var fuelRate = 1.0
        var fuelEff = 1.0
        var timeMod = 1.0

        if (hasSpeed) {
            speedMult *= 2.0
            timeMod *= 0.5
            fuelRate *= 1.5
        }

        if (hasFuel) {
            fuelEff *= 2.0
            timeMod *= 1.25
        }

        return UpgradeEffects(
            mode = FurnaceMode.NORMAL,
            speedMultiplier = speedMult,
            fuelConsumptionRate = fuelRate,
            fuelEfficiencyBonus = fuelEff,
            smeltTimeModifier = timeMod,
            canSmelt = true
        )
    }

    private fun createBlastEffects(hasSpeed: Boolean, hasFuel: Boolean): UpgradeEffects {
        var speedMult = 2.0
        var fuelRate = 2.0
        var fuelEff = 1.0
        var timeMod = 0.5

        if (hasSpeed && hasFuel) {
            speedMult = 1.6
            timeMod = 0.5 * 1.25
        } else if (hasSpeed) {
            speedMult = 4.0
            timeMod = 0.5 * 0.5
            fuelRate = 3.0
        } else if (hasFuel) {
            speedMult = 1.6
            timeMod = 0.5 * 1.25
        }

        return UpgradeEffects(
            mode = FurnaceMode.BLAST,
            speedMultiplier = speedMult,
            fuelConsumptionRate = fuelRate,
            fuelEfficiencyBonus = fuelEff,
            smeltTimeModifier = timeMod,
            canSmelt = true
        )
    }

    private fun createSmokerEffects(hasSpeed: Boolean, hasFuel: Boolean): UpgradeEffects {
        var speedMult = 2.0
        var fuelRate = 2.0
        var fuelEff = 1.0
        var timeMod = 0.5

        if (hasSpeed && hasFuel) {
            speedMult = 1.6
            timeMod = 0.5 * 1.25
        } else if (hasSpeed) {
            speedMult = 4.0
            timeMod = 0.5 * 0.5
            fuelRate = 3.0
        } else if (hasFuel) {
            speedMult = 1.6
            timeMod = 0.5 * 1.25
        }

        return UpgradeEffects(
            mode = FurnaceMode.SMOKER,
            speedMultiplier = speedMult,
            fuelConsumptionRate = fuelRate,
            fuelEfficiencyBonus = fuelEff,
            smeltTimeModifier = timeMod,
            canSmelt = true
        )
    }

    private fun createIndustrialEffects(
        hasBlast: Boolean,
        hasSmoker: Boolean,
        hasSpeed: Boolean,
        hasFuel: Boolean,
        hasGenerator: Boolean
    ): UpgradeEffects {
        var speedMult = 1.0
        var timeMod = 1.0

        if (hasSpeed) {
            speedMult *= 2.0
            timeMod *= 0.5
        }

        if (hasFuel) {
            timeMod *= 1.25
        }

        val mode = when {
            hasBlast -> FurnaceMode.BLAST
            hasSmoker -> FurnaceMode.SMOKER
            else -> FurnaceMode.INDUSTRIAL
        }

        return UpgradeEffects(
            mode = mode,
            speedMultiplier = speedMult,
            smeltTimeModifier = timeMod,
            outputSlots = 3,
            usesEnergy = !hasGenerator,
            canSmelt = !hasGenerator
        )
    }

    private fun createGeneratorOnlyEffects(
        hasSpeed: Boolean,
        hasFuel: Boolean,
        hasIndustrial: Boolean
    ): UpgradeEffects {
        var powerMult = 1.0
        var speedMult = 1.0

        if (hasSpeed) {
            powerMult *= 0.25
            speedMult *= 2.0
        }

        if (hasFuel) {
            powerMult *= 2.0
            speedMult *= 0.75
        }

        return UpgradeEffects(
            mode = FurnaceMode.GENERATOR_ONLY,
            generatorPowerMultiplier = powerMult,
            generatorSpeedMultiplier = speedMult,
            canSmelt = false,
            outputSlots = if (hasIndustrial) 3 else 1
        )
    }

    private fun createGeneratorBlastEffects(
        hasSpeed: Boolean,
        hasFuel: Boolean,
        hasIndustrial: Boolean
    ): UpgradeEffects {
        var powerMult = 1.0
        var speedMult = 1.0

        if (hasSpeed) {
            powerMult *= 0.25
            speedMult *= 2.0
        }

        if (hasFuel) {
            powerMult *= 2.0
            speedMult *= 0.75
        }

        return UpgradeEffects(
            mode = FurnaceMode.GENERATOR_BLAST,
            generatorPowerMultiplier = powerMult,
            generatorSpeedMultiplier = speedMult,
            canSmelt = false,
            outputSlots = if (hasIndustrial) 3 else 1
        )
    }

    private fun createGeneratorSmokerEffects(
        hasSpeed: Boolean,
        hasFuel: Boolean,
        hasIndustrial: Boolean
    ): UpgradeEffects {
        var powerMult = 1.0
        var speedMult = 1.0

        if (hasSpeed) {
            powerMult *= 0.25
            speedMult *= 2.0
        }

        if (hasFuel) {
            powerMult *= 2.0
            speedMult *= 0.75
        }

        return UpgradeEffects(
            mode = FurnaceMode.GENERATOR_SMOKER,
            generatorPowerMultiplier = powerMult,
            generatorSpeedMultiplier = speedMult,
            canSmelt = false,
            outputSlots = if (hasIndustrial) 3 else 1
        )
    }

    fun invalidateCache() {
        cachedEffects = null
        lastSlotHash = 0
    }

    fun getDisplayBlockType(): String {
        val effects = calculateEffects()
        return when (effects.mode) {
            FurnaceMode.BLAST, FurnaceMode.GENERATOR_BLAST -> "blast_furnace"
            FurnaceMode.SMOKER, FurnaceMode.GENERATOR_SMOKER -> "smoker"
            else -> "furnace"
        }
    }

    fun isRecipeCompatible(recipeType: RecipeCompatibility): Boolean {
        val effects = calculateEffects()
        return when (effects.mode) {
            FurnaceMode.NORMAL, FurnaceMode.INDUSTRIAL -> true
            FurnaceMode.BLAST -> recipeType == RecipeCompatibility.BLAST || recipeType == RecipeCompatibility.ANY
            FurnaceMode.SMOKER -> recipeType == RecipeCompatibility.SMOKER || recipeType == RecipeCompatibility.ANY
            FurnaceMode.GENERATOR_ONLY,
            FurnaceMode.GENERATOR_BLAST,
            FurnaceMode.GENERATOR_SMOKER -> false
        }
    }

    enum class RecipeCompatibility {
        NORMAL,
        BLAST,
        SMOKER,
        ANY
    }
}

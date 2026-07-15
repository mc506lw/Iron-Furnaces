package top.mc506lw.rebar.ironfurnaces.furnace

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import top.mc506lw.rebar.ironfurnaces.upgrades.UpgradeType

class UpgradeEffectCalculatorTest {
    @Test
    fun `normal furnace has neutral effects`() {
        assertEquals(UpgradeEffects(), UpgradeEffectCalculator.calculate(null, null, null))
    }

    @Test
    fun `speed and fuel upgrades preserve their balancing tradeoffs`() {
        val speed = UpgradeEffectCalculator.calculate(null, UpgradeType.SPEED, null)
        assertEquals(2.0, speed.speedMultiplier)
        assertEquals(1.5, speed.fuelConsumptionRate)
        assertEquals(0.5, speed.smeltTimeModifier)

        val fuel = UpgradeEffectCalculator.calculate(null, UpgradeType.FUEL, null)
        assertEquals(2.0, fuel.fuelEfficiencyBonus)
        assertEquals(1.25, fuel.smeltTimeModifier)
    }

    @Test
    fun `specialized furnace modes keep their recipe restrictions`() {
        val blast = UpgradeEffectCalculator.calculate(UpgradeType.BLAST, UpgradeType.SPEED, null)
        assertEquals(FurnaceMode.BLAST, blast.mode)
        assertEquals(4.0, blast.speedMultiplier)
        assertEquals(3.0, blast.fuelConsumptionRate)
        assertEquals(0.25, blast.smeltTimeModifier)

        val smoker = UpgradeEffectCalculator.calculate(UpgradeType.SMOKER, null, null)
        assertEquals(FurnaceMode.SMOKER, smoker.mode)
    }

    @Test
    fun `industrial and generator modes expose the expected capabilities`() {
        val industrial = UpgradeEffectCalculator.calculate(null, null, UpgradeType.INDUSTRIAL)
        assertEquals(FurnaceMode.INDUSTRIAL, industrial.mode)
        assertEquals(3, industrial.outputSlots)
        assertTrue(industrial.usesEnergy)
        assertTrue(industrial.canSmelt)

        val generator = UpgradeEffectCalculator.calculate(
            UpgradeType.BLAST,
            UpgradeType.FUEL,
            UpgradeType.GENERATOR
        )
        assertEquals(FurnaceMode.GENERATOR_BLAST, generator.mode)
        assertEquals(2.0, generator.generatorPowerMultiplier)
        assertEquals(0.75, generator.generatorSpeedMultiplier)
        assertFalse(generator.canSmelt)
    }
}

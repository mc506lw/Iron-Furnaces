package top.mc506lw.rebar.ironfurnaces.upgrades

import org.bukkit.NamespacedKey
import top.mc506lw.rebar.ironfurnaces.IronFurnaceKeys

enum class UpgradeSlot(val inventoryIndex: Int) {
    RED(0),
    GREEN(1),
    BLUE(2)
}

enum class UpgradeType(
    val key: NamespacedKey,
    val slot: UpgradeSlot
) {
    BLAST(IronFurnaceKeys.BLAST_UPGRADE, UpgradeSlot.RED),
    SMOKER(IronFurnaceKeys.SMOKER_UPGRADE, UpgradeSlot.RED),
    SPEED(IronFurnaceKeys.SPEED_UPGRADE, UpgradeSlot.GREEN),
    FUEL(IronFurnaceKeys.FUEL_UPGRADE, UpgradeSlot.GREEN),
    INDUSTRIAL(IronFurnaceKeys.INDUSTRIAL_UPGRADE, UpgradeSlot.BLUE),
    GENERATOR(IronFurnaceKeys.GENERATOR_UPGRADE, UpgradeSlot.BLUE)
}

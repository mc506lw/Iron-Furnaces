package top.mc506lw.rebar.ironfurnaces.upgrades

import io.github.pylonmc.rebar.item.RebarItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class UpgradeType(
    val displayName: String,
    val description: String,
    val key: org.bukkit.NamespacedKey
) {
    BLAST("高炉组件", "允许熔炼矿石类物品（如铁矿石→铁锭）2倍速度", top.mc506lw.rebar.ironfurnaces.IronFurnaceKeys.BLAST_UPGRADE),
    SMOKER("烟熏炉组件", "允许烹饪食物类物品 2倍速度", top.mc506lw.rebar.ironfurnaces.IronFurnaceKeys.SMOKER_UPGRADE),
    SPEED("速度升级", "整体熔炼速度提升 50%", top.mc506lw.rebar.ironfurnaces.IronFurnaceKeys.SPEED_UPGRADE),
    FUEL("燃料升级", "燃料效率提升 100%（燃烧时间翻倍）", top.mc506lw.rebar.ironfurnaces.IronFurnaceKeys.FUEL_UPGRADE),
    INDUSTRIAL("工业升级", "增加输出槽位至 3 个，支持批量处理", top.mc506lw.rebar.ironfurnaces.IronFurnaceKeys.INDUSTRIAL_UPGRADE),
    GENERATOR("发电机升级", "使用红石信号自动运行，无需燃料", top.mc506lw.rebar.ironfurnaces.IronFurnaceKeys.GENERATOR_UPGRADE)
}

data class FurnaceUpgrade(val type: UpgradeType, val level: Int = 1) {

    fun getSpeedBonus(): Double = when (type) {
        UpgradeType.BLAST -> if (level > 0) 2.0 else 1.0
        UpgradeType.SMOKER -> if (level > 0) 2.0 else 1.0
        UpgradeType.SPEED -> 1.0 + (0.5 * level)
        else -> 1.0
    }

    fun getFuelBonus(): Double = when (type) {
        UpgradeType.FUEL -> 1.0 + (1.0 * level)
        else -> 1.0
    }

    fun getOutputSlots(): Int = when (type) {
        UpgradeType.INDUSTRIAL -> 1 + (2 * level)
        else -> 1
    }

    fun isAutoFuel(): Boolean = type == UpgradeType.GENERATOR && level > 0
}

class BlastUpgradeItem : RebarItem(ItemStack(Material.BLAST_FURNACE))
class SmokerUpgradeItem : RebarItem(ItemStack(Material.SMOKER))
class SpeedUpgradeItem : RebarItem(ItemStack(org.bukkit.Material.SUGAR))
class FuelUpgradeItem : RebarItem(ItemStack(Material.COAL_BLOCK))
class IndustrialUpgradeItem : RebarItem(ItemStack(Material.IRON_BLOCK))
class GeneratorUpgradeItem : RebarItem(ItemStack(Material.REDSTONE_BLOCK))

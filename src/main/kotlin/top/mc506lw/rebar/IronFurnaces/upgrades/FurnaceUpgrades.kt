package top.mc506lw.rebar.ironfurnaces.upgrades

import io.github.pylonmc.rebar.item.RebarItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

enum class UpgradeType(
    val displayName: String,
    val description: String,
    val key: org.bukkit.NamespacedKey
) {
    BLAST("高炉组件", "将熔炉变为高炉，仅能熔炼矿石，速度翻倍", top.mc506lw.rebar.ironfurnaces.IronFurnaceKeys.BLAST_UPGRADE),
    SMOKER("烟熏炉组件", "将熔炉变为烟熏炉，仅能烹饪食物，速度翻倍", top.mc506lw.rebar.ironfurnaces.IronFurnaceKeys.SMOKER_UPGRADE),
    SPEED("速度升级", "烧炼时间减半，燃料消耗加倍", top.mc506lw.rebar.ironfurnaces.IronFurnaceKeys.SPEED_UPGRADE),
    FUEL("燃料升级", "燃料消耗减半，烧炼时间增加1/4", top.mc506lw.rebar.ironfurnaces.IronFurnaceKeys.FUEL_UPGRADE),
    INDUSTRIAL("工业升级", "消耗能量而非燃料，增加输出槽位至3个", top.mc506lw.rebar.ironfurnaces.IronFurnaceKeys.INDUSTRIAL_UPGRADE),
    GENERATOR("发电机升级", "不可熔炼，将热量转化为能量", top.mc506lw.rebar.ironfurnaces.IronFurnaceKeys.GENERATOR_UPGRADE)
}

data class FurnaceUpgrade(val type: UpgradeType, val level: Int = 1) {

    fun getSpeedBonus(): Double = when (type) {
        UpgradeType.BLAST -> if (level > 0) 2.0 else 1.0
        UpgradeType.SMOKER -> if (level > 0) 2.0 else 1.0
        UpgradeType.SPEED -> 2.0
        else -> 1.0
    }

    fun getFuelBonus(): Double = when (type) {
        UpgradeType.FUEL -> 2.0
        else -> 1.0
    }

    fun getOutputSlots(): Int = when (type) {
        UpgradeType.INDUSTRIAL -> 3
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

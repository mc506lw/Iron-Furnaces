package top.mc506lw.rebar.ironfurnaces

import org.bukkit.NamespacedKey

object IronFurnaceKeys {

    fun key(name: String) = NamespacedKey("ironfurnaces", name)

    val RAINBOW_CORE = key("rainbow_core")
    val RAINBOW_SHELL = key("rainbow_shell")
    val RAINBOW_COAL = key("rainbow_coal")
    val RAINBOW_CONNECTOR = key("rainbow_connector")

    val COPPER_FURNACE = key("copper_furnace")
    val IRON_FURNACE = key("iron_furnace")
    val GOLD_FURNACE = key("gold_furnace")
    val DIAMOND_FURNACE = key("diamond_furnace")
    val EMERALD_FURNACE = key("emerald_furnace")
    val CRYSTAL_FURNACE = key("crystal_furnace")
    val OBSIDIAN_FURNACE = key("obsidian_furnace")
    val NETHERITE_FURNACE = key("netherite_furnace")
    val RAINBOW_FURNACE = key("rainbow_furnace")

    val BLAST_UPGRADE = key("blast_upgrade")
    val SMOKER_UPGRADE = key("smoker_upgrade")
    val SPEED_UPGRADE = key("speed_upgrade")
    val FUEL_UPGRADE = key("fuel_upgrade")
    val INDUSTRIAL_UPGRADE = key("industrial_upgrade")
    val GENERATOR_UPGRADE = key("generator_upgrade")
}

package top.mc506lw.rebar.ironfurnaces.furnace

import java.util.Locale

enum class FurnaceTier(val smeltTimePerItem: Int) {
    COPPER(180),
    IRON(160),
    GOLD(120),
    DIAMOND(80),
    EMERALD(40),
    CRYSTAL(40),
    OBSIDIAN(20),
    NETHERITE(5),
    RAINBOW(20);

    val speedMultiplier: Double = 200.0 / smeltTimePerItem

    val speedLabel: String = if (speedMultiplier == speedMultiplier.toInt().toDouble()) {
        "${speedMultiplier.toInt()}x"
    } else {
        "%.1fx".format(Locale.ROOT, speedMultiplier)
    }
}

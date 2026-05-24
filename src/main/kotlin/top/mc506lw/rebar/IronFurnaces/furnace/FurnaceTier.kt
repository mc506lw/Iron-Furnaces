package top.mc506lw.rebar.ironfurnaces.furnace

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

    val speedMultiplier: Double
        get() = 200.0 / smeltTimePerItem

    val speedLabel: String
        get() {
            val mult = speedMultiplier
            return if (mult == mult.toInt().toDouble()) "${mult.toInt()}x" else "${"%.1f".format(mult)}x"
        }
}
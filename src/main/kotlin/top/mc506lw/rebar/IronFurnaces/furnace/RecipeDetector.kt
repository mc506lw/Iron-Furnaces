package top.mc506lw.rebar.ironfurnaces.furnace

import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import top.mc506lw.rebar.ironfurnaces.furnace.UpgradeEffectManager.RecipeCompatibility

object RecipeDetector {
    private val LOGGER = java.util.logging.Logger.getLogger("IronFurnace-Recipes")

    private val recipeCache = mutableMapOf<Material, RecipeCompatibility>()

    private val ORE_MATERIALS = setOf(
        Material.IRON_ORE,
        Material.GOLD_ORE,
        Material.DIAMOND_ORE,
        Material.EMERALD_ORE,
        Material.COAL_ORE,
        Material.LAPIS_ORE,
        Material.REDSTONE_ORE,
        Material.COPPER_ORE,
        Material.NETHER_GOLD_ORE,
        Material.NETHER_QUARTZ_ORE,
        Material.ANCIENT_DEBRIS,
        Material.RAW_IRON,
        Material.RAW_GOLD,
        Material.RAW_COPPER
    )

    private val FOOD_MATERIALS = setOf(
        Material.BEEF,
        Material.PORKCHOP,
        Material.CHICKEN,
        Material.MUTTON,
        Material.RABBIT,
        Material.COD,
        Material.SALMON,
        Material.TROPICAL_FISH,
        Material.PUFFERFISH,
        Material.KELP,
        Material.POTATO
    )

    private val LOG_MATERIALS = setOf(
        Material.OAK_LOG,
        Material.SPRUCE_LOG,
        Material.BIRCH_LOG,
        Material.JUNGLE_LOG,
        Material.ACACIA_LOG,
        Material.DARK_OAK_LOG,
        Material.MANGROVE_LOG,
        Material.CHERRY_LOG,
        Material.CRIMSON_STEM,
        Material.WARPED_STEM
    )

    fun detectRecipeCompatibility(stack: ItemStack?): RecipeCompatibility {
        if (stack == null) return RecipeCompatibility.ANY
        if (stack.isEmpty) return RecipeCompatibility.ANY

        val material = stack.type
        recipeCache[material]?.let { return it }

        val compatibility = classifyMaterial(material)
        recipeCache[material] = compatibility
        return compatibility
    }

    private fun classifyMaterial(material: Material): RecipeCompatibility {
        return when {
            material in ORE_MATERIALS -> RecipeCompatibility.BLAST
            material.name.endsWith("_ORE") -> RecipeCompatibility.BLAST
            material.name.startsWith("RAW_") -> RecipeCompatibility.BLAST
            material in FOOD_MATERIALS || isFoodMaterial(material) -> RecipeCompatibility.SMOKER
            material in LOG_MATERIALS || isLogMaterial(material) -> RecipeCompatibility.SMOKER
            else -> RecipeCompatibility.NORMAL
        }
    }

    private fun isFoodMaterial(material: Material): Boolean {
        try {
            return material.isEdible
        } catch (e: Exception) {
            return false
        }
    }

    private fun isLogMaterial(material: Material): Boolean {
        return material.name.endsWith("_LOG") ||
               material.name.endsWith("_WOOD") ||
               material.name.endsWith("_STEM") ||
               material.name == "MUSHROOM_STEM"
    }

    fun canSmeltInBlastFurnace(stack: ItemStack): Boolean {
        return detectRecipeCompatibility(stack) in listOf(
            RecipeCompatibility.BLAST,
            RecipeCompatibility.ANY
        )
    }

    fun canSmeltInSmoker(stack: ItemStack): Boolean {
        return detectRecipeCompatibility(stack) in listOf(
            RecipeCompatibility.SMOKER,
            RecipeCompatibility.ANY
        )
    }

    fun clearCache() {
        recipeCache.clear()
    }
}

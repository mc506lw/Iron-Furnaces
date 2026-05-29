package top.mc506lw.rebar.ironfurnaces

import io.github.pylonmc.rebar.recipe.RecipeType
import org.bukkit.Material
import org.bukkit.inventory.ShapedRecipe
import org.bukkit.inventory.recipe.CraftingBookCategory

object FurnaceRecipes {

    fun initialize() {
        initCopperFurnace()
        initIronFurnace()
        initGoldFurnace()
        initDiamondFurnace()
        initEmeraldFurnace()
        initObsidianFurnace()
        initCrystalFurnace()
        initNetheriteFurnace()
    }

    private fun initCopperFurnace() {
        val recipe = ShapedRecipe(IronFurnaceKeys.COPPER_FURNACE, IronFurnaceItems.COPPER_FURNACE_ITEM)
        recipe.shape("CCC", "CFC", "CCC")
        recipe.setIngredient('C', Material.COPPER_INGOT)
        recipe.setIngredient('F', Material.FURNACE)
        recipe.setCategory(CraftingBookCategory.MISC)
        RecipeType.VANILLA_SHAPED.addRecipe(recipe)
    }

    private fun initIronFurnace() {
        val recipe = ShapedRecipe(IronFurnaceKeys.IRON_FURNACE, IronFurnaceItems.IRON_FURNACE_ITEM)
        recipe.shape("III", "ICI", "III")
        recipe.setIngredient('I', Material.IRON_INGOT)
        recipe.setIngredient('C', IronFurnaceItems.COPPER_FURNACE_ITEM)
        recipe.setCategory(CraftingBookCategory.MISC)
        RecipeType.VANILLA_SHAPED.addRecipe(recipe)
    }

    private fun initGoldFurnace() {
        val recipe = ShapedRecipe(IronFurnaceKeys.GOLD_FURNACE, IronFurnaceItems.GOLD_FURNACE_ITEM)
        recipe.shape("GGG", "GIG", "GBG")
        recipe.setIngredient('G', Material.GOLD_INGOT)
        recipe.setIngredient('I', IronFurnaceItems.IRON_FURNACE_ITEM)
        recipe.setIngredient('B', Material.GOLD_BLOCK)
        recipe.setCategory(CraftingBookCategory.MISC)
        RecipeType.VANILLA_SHAPED.addRecipe(recipe)
    }

    private fun initDiamondFurnace() {
        val recipe = ShapedRecipe(IronFurnaceKeys.DIAMOND_FURNACE, IronFurnaceItems.DIAMOND_FURNACE_ITEM)
        recipe.shape("DDD", "GFG", "DDD")
        recipe.setIngredient('D', Material.DIAMOND)
        recipe.setIngredient('G', Material.GLASS)
        recipe.setIngredient('F', IronFurnaceItems.GOLD_FURNACE_ITEM)
        recipe.setCategory(CraftingBookCategory.MISC)
        RecipeType.VANILLA_SHAPED.addRecipe(recipe)
    }

    private fun initEmeraldFurnace() {
        val recipe = ShapedRecipe(IronFurnaceKeys.EMERALD_FURNACE, IronFurnaceItems.EMERALD_FURNACE_ITEM)
        recipe.shape("EEE", "EDE", "EEE")
        recipe.setIngredient('E', Material.EMERALD)
        recipe.setIngredient('D', IronFurnaceItems.DIAMOND_FURNACE_ITEM)
        recipe.setCategory(CraftingBookCategory.MISC)
        RecipeType.VANILLA_SHAPED.addRecipe(recipe)
    }

    private fun initObsidianFurnace() {
        val recipe = ShapedRecipe(IronFurnaceKeys.OBSIDIAN_FURNACE, IronFurnaceItems.OBSIDIAN_FURNACE_ITEM)
        recipe.shape("OBO", "BFO", "OBO")
        recipe.setIngredient('O', Material.OBSIDIAN)
        recipe.setIngredient('B', Material.BLAZE_ROD)
        recipe.setIngredient('F', IronFurnaceItems.DIAMOND_FURNACE_ITEM)
        recipe.setCategory(CraftingBookCategory.MISC)
        RecipeType.VANILLA_SHAPED.addRecipe(recipe)
    }

    private fun initCrystalFurnace() {
        val recipe = ShapedRecipe(IronFurnaceKeys.CRYSTAL_FURNACE, IronFurnaceItems.CRYSTAL_FURNACE_ITEM)
        recipe.shape("GGG", "GDG", "GEG")
        recipe.setIngredient('G', Material.GLASS)
        recipe.setIngredient('D', IronFurnaceItems.DIAMOND_FURNACE_ITEM)
        recipe.setIngredient('E', Material.ENDER_EYE)
        recipe.setCategory(CraftingBookCategory.MISC)
        RecipeType.VANILLA_SHAPED.addRecipe(recipe)
    }

    private fun initNetheriteFurnace() {
        val recipe = ShapedRecipe(IronFurnaceKeys.NETHERITE_FURNACE, IronFurnaceItems.NETHERITE_FURNACE_ITEM)
        recipe.shape("NMN", "MFM", "NSN")
        recipe.setIngredient('N', Material.NETHERITE_INGOT)
        recipe.setIngredient('M', Material.MAGMA_CREAM)
        recipe.setIngredient('F', IronFurnaceItems.OBSIDIAN_FURNACE_ITEM)
        recipe.setIngredient('S', Material.SOUL_SAND)
        recipe.setCategory(CraftingBookCategory.MISC)
        RecipeType.VANILLA_SHAPED.addRecipe(recipe)
    }
}

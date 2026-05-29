package top.mc506lw.rebar.ironfurnaces

import io.github.pylonmc.rebar.guide.pages.base.SimpleStaticGuidePage
import org.bukkit.inventory.ItemStack
import java.lang.reflect.Method

object IronFurnacePages {

    val PAGE = SimpleStaticGuidePage(IronFurnaceKeys.key("ironfurnaces"))

    fun initialise() {
        PAGE.addItem(IronFurnaceItems.COPPER_FURNACE_ITEM)
        PAGE.addItem(IronFurnaceItems.IRON_FURNACE_ITEM)
        PAGE.addItem(IronFurnaceItems.GOLD_FURNACE_ITEM)
        PAGE.addItem(IronFurnaceItems.DIAMOND_FURNACE_ITEM)
        PAGE.addItem(IronFurnaceItems.EMERALD_FURNACE_ITEM)
        PAGE.addItem(IronFurnaceItems.CRYSTAL_FURNACE_ITEM)
        PAGE.addItem(IronFurnaceItems.OBSIDIAN_FURNACE_ITEM)
        PAGE.addItem(IronFurnaceItems.NETHERITE_FURNACE_ITEM)
        PAGE.addItem(IronFurnaceItems.RAINBOW_FURNACE_ITEM)

        PAGE.addItem(IronFurnaceItems.BLAST_UPGRADE)
        PAGE.addItem(IronFurnaceItems.SMOKER_UPGRADE)
        PAGE.addItem(IronFurnaceItems.SPEED_UPGRADE)
        PAGE.addItem(IronFurnaceItems.FUEL_UPGRADE)
        PAGE.addItem(IronFurnaceItems.INDUSTRIAL_UPGRADE)
        PAGE.addItem(IronFurnaceItems.GENERATOR_UPGRADE)

        PAGE.addItem(IronFurnaceItems.RAINBOW_CORE)
        PAGE.addItem(IronFurnaceItems.RAINBOW_SHELL)
        PAGE.addItem(IronFurnaceItems.RAINBOW_COAL)
        PAGE.addItem(IronFurnaceItems.RAINBOW_CONNECTOR)

        val rebarGuideClass = Class.forName("io.github.pylonmc.rebar.content.guide.RebarGuide")
        val getRootPageMethod: Method = rebarGuideClass.getMethod("getRootPage")
        val rootPage = getRootPageMethod.invoke(null)
        val addPageMethod: Method = rootPage.javaClass.getMethod("addPage", ItemStack::class.java, Class.forName("io.github.pylonmc.rebar.guide.pages.base.GuidePage"))
        addPageMethod.invoke(rootPage, IronFurnaceItems.IRON_FURNACE_ITEM, PAGE)
    }
}

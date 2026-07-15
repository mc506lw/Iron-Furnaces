package top.mc506lw.rebar.ironfurnaces.furnace

import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.pylonmc.rebar.util.gui.GuiItems
import io.github.pylonmc.rebar.util.gui.ProgressItem
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.inventory.ClickType
import xyz.xenondevs.invui.Click
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.inventory.VirtualInventory
import xyz.xenondevs.invui.item.AbstractItem
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.window.Window

class FurnaceGuiFactory(
    private val furnace: AbstractIronFurnace,
    private val inputInv: VirtualInventory,
    private val outputInv: VirtualInventory,
    private val fuelInv: VirtualInventory,
    private val fuelSystem: FurnaceFuelSystem,
    private val recipeProgressItem: ProgressItem,
    private val upgradeRedSlot: VirtualInventory,
    private val upgradeGreenSlot: VirtualInventory,
    private val upgradeBlueSlot: VirtualInventory
) {
    private val upgradeButton = navigationItem(
        ItemStackBuilder.of(Material.ANVIL)
            .name(Component.translatable("ironfurnaces.gui.upgrade.name"))
            .lore(Component.translatable("ironfurnaces.gui.upgrade.lore"))
    ) { player -> openUpgradeGui(player) }

    private val backButton = navigationItem(
        ItemStackBuilder.of(Material.ARROW)
            .name(Component.translatable("ironfurnaces.gui.back_button.name"))
    ) { player -> openMainGui(player) }

    private val redInfo = ItemStackBuilder.of(Material.RED_STAINED_GLASS_PANE)
        .name(Component.translatable("ironfurnaces.gui.upgrade_red.name"))
        .lore(Component.translatable("ironfurnaces.gui.upgrade_red.lore"))

    private val greenInfo = ItemStackBuilder.of(Material.LIME_STAINED_GLASS_PANE)
        .name(Component.translatable("ironfurnaces.gui.upgrade_green.name"))
        .lore(Component.translatable("ironfurnaces.gui.upgrade_green.lore"))

    private val blueInfo = ItemStackBuilder.of(Material.BLUE_STAINED_GLASS_PANE)
        .name(Component.translatable("ironfurnaces.gui.upgrade_blue.name"))
        .lore(Component.translatable("ironfurnaces.gui.upgrade_blue.lore"))

    fun createMainGui(): Gui {
        val structure = if (furnace.displayedOutputSlots == 1) {
            arrayOf(
                "# # # # # # # # U",
                "# # i # # # # # #",
                "# # > # S # o # #",
                "# # f # # # # # #",
                "# # # # # # # # #"
            )
        } else {
            arrayOf(
                "# # # # # # # # U",
                "# # i # # # o # #",
                "# # > # S # o # #",
                "# # f # # # o # #",
                "# # # # # # # # #"
            )
        }

        return Gui.builder()
            .setStructure(*structure)
            .addIngredient('#', GuiItems.background())
            .addIngredient('U', upgradeButton)
            .addIngredient('i', inputInv)
            .addIngredient('>', fuelSystem.fuelProgressItem)
            .addIngredient('S', recipeProgressItem)
            .addIngredient('o', outputInv)
            .addIngredient('f', fuelInv)
            .build()
    }

    fun createUpgradeGui(): Gui = Gui.builder()
        .setStructure(
            "# # # # # # # # <",
            "# # R # G # L # #",
            "# # r # g # b # #",
            "# # # # # # # # #"
        )
        .addIngredient('#', GuiItems.background())
        .addIngredient('<', backButton)
        .addIngredient('R', redInfo)
        .addIngredient('G', greenInfo)
        .addIngredient('L', blueInfo)
        .addIngredient('r', upgradeRedSlot)
        .addIngredient('g', upgradeGreenSlot)
        .addIngredient('b', upgradeBlueSlot)
        .build()

    private fun openMainGui(player: Player) {
        Window.builder()
            .setUpperGui(createMainGui())
            .setTitle(furnace.getGuiTitle())
            .setViewer(player)
            .build()
            .open()
    }

    private fun openUpgradeGui(player: Player) {
        Window.builder()
            .setUpperGui(createUpgradeGui())
            .setTitle(furnace.getGuiTitle())
            .setViewer(player)
            .build()
            .open()
    }

    private fun navigationItem(provider: ItemProvider, navigate: (Player) -> Unit): Item =
        object : AbstractItem() {
            override fun getItemProvider(viewer: Player): ItemProvider = provider

            override fun handleClick(clickType: ClickType, player: Player, click: Click) {
                if (clickType.isLeftClick) navigate(player)
            }
        }
}

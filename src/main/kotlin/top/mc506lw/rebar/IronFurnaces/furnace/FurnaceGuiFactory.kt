package top.mc506lw.rebar.ironfurnaces.furnace

import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.pylonmc.rebar.util.gui.GuiItems
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import xyz.xenondevs.invui.Click
import xyz.xenondevs.invui.gui.Gui
import xyz.xenondevs.invui.inventory.VirtualInventory
import xyz.xenondevs.invui.item.AbstractItem
import xyz.xenondevs.invui.item.Item
import xyz.xenondevs.invui.item.ItemProvider
import xyz.xenondevs.invui.window.Window

open class FurnaceGuiFactory(
    private val furnace: AbstractIronFurnace,
    private val guiMaterial: Material,
    private val inputInv: VirtualInventory,
    private val outputInv: VirtualInventory,
    private val fuelInv: VirtualInventory,
    private val fuelSystem: FurnaceFuelSystem,
    private val recipeProgressItem: io.github.pylonmc.rebar.util.gui.ProgressItem,
    private val upgradeRedSlot: VirtualInventory,
    private val upgradeGreenSlot: VirtualInventory,
    private val upgradeBlueSlot: VirtualInventory
) {
    fun createMainGui(): Gui {
        val upgradeButton: Item = object : AbstractItem() {
            override fun getItemProvider(viewer: Player): ItemProvider {
                return ItemStackBuilder.of(ItemStack(Material.ANVIL))
                    .name(Component.translatable("ironfurnaces.gui.upgrade.name"))
                    .lore(Component.translatable("ironfurnaces.gui.upgrade.lore"))
            }

            override fun handleClick(clickType: org.bukkit.event.inventory.ClickType, player: Player, click: Click) {
                if (clickType.isLeftClick) {
                    Window.builder()
                        .setUpperGui(createUpgradeGui())
                        .setTitle(furnace.getGuiTitle())
                        .setViewer(player)
                        .build()
                        .open()
                }
            }
        }

        return Gui.builder()
            .setStructure(
                "# # # # # # # # U",
                "# # i # # # # # #",
                "# # > # S # o # #",
                "# # f # # # # # #",
                "# # # # # # # # #"
            )
            .addIngredient('#', GuiItems.background())
            .addIngredient('U', upgradeButton)
            .addIngredient('i', inputInv)
            .addIngredient('>', fuelSystem.fuelProgressItem)
            .addIngredient('S', recipeProgressItem)
            .addIngredient('o', outputInv)
            .addIngredient('f', fuelInv)
            .build()
    }

    open fun createUpgradeGui(): Gui {
        val redInfo = ItemStackBuilder.of(ItemStack(Material.RED_STAINED_GLASS_PANE))
            .name(Component.translatable("ironfurnaces.gui.upgrade_red.name"))
            .lore(Component.translatable("ironfurnaces.gui.upgrade_red.lore"))

        val greenInfo = ItemStackBuilder.of(ItemStack(Material.LIME_STAINED_GLASS_PANE))
            .name(Component.translatable("ironfurnaces.gui.upgrade_green.name"))
            .lore(Component.translatable("ironfurnaces.gui.upgrade_green.lore"))

        val blueInfo = ItemStackBuilder.of(ItemStack(Material.BLUE_STAINED_GLASS_PANE))
            .name(Component.translatable("ironfurnaces.gui.upgrade_blue.name"))
            .lore(Component.translatable("ironfurnaces.gui.upgrade_blue.lore"))

        val backButton: Item = object : AbstractItem() {
            override fun getItemProvider(viewer: Player): ItemProvider =
                ItemStackBuilder.of(ItemStack(Material.ARROW))
                    .name(Component.translatable("ironfurnaces.gui.back_button.name"))

            override fun handleClick(clickType: org.bukkit.event.inventory.ClickType, player: Player, click: Click) {
                if (clickType.isLeftClick) {
                    Window.builder()
                        .setUpperGui(createMainGui())
                        .setTitle(furnace.getGuiTitle())
                        .setViewer(player)
                        .build()
                        .open()
                }
            }
        }

        return Gui.builder()
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
    }
}

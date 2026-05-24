package top.mc506lw.rebar.ironfurnaces.furnace

import io.github.pylonmc.rebar.item.builder.ItemStackBuilder
import io.github.pylonmc.rebar.util.gui.GuiItems
import io.github.pylonmc.rebar.util.gui.ProgressItem
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.Component.text
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.inventory.ItemStack
import org.bukkit.craftbukkit.inventory.CraftItemStack
import net.minecraft.server.level.ServerLevel
import xyz.xenondevs.invui.inventory.VirtualInventory

class FurnaceFuelSystem(
    private val block: Block,
    private val furnaceTier: FurnaceTier,
    private val fuelInv: VirtualInventory,
    private val inputInv: VirtualInventory,
    var fuelEfficiency: Double = 1.0
) {
    companion object {
        private val LOGGER = java.util.logging.Logger.getLogger("IronFurnace")
    }

    var currentFuelTime = 0
        internal set
    var fuelRemaining = 0
        internal set
    private var burningFuelType: Material? = null

    val fuelProgressItem = ProgressItem(GuiItems.background())

    private val noFuelDisplay = ItemStackBuilder
        .gui(Material.BLAZE_POWDER, "${furnaceTier.name.lowercase()}_fuel_status")
        .name(Component.translatable("ironfurnaces.gui.fuel_status.name"))
        .lore(
            Component.translatable(
                "ironfurnaces.gui.fuel_status.lore",
                io.github.pylonmc.rebar.i18n.RebarArgument.of("fuel", Component.translatable("ironfurnaces.gui.no_fuel"))
            )
        )

    val isBurning: Boolean
        get() = fuelRemaining > 0

    fun consumeFuel() {
        if (fuelRemaining > 0) return

        val inputStack = inputInv.getItem(0)
        val hasInput = inputStack != null && !inputStack.isEmpty()

        if (!hasInput) return

        val fuelStack = fuelInv.getItem(0) ?: return
        if (fuelStack.isEmpty()) return

        val fuelTime = getFuelTimeFromServer(fuelStack)
        if (fuelTime <= 0) return

        if (fuelStack.amount > 1) {
            fuelStack.amount = fuelStack.amount - 1
            fuelInv.setItem(io.github.pylonmc.rebar.util.MachineUpdateReason(), 0, fuelStack)
        } else {
            fuelInv.setItem(io.github.pylonmc.rebar.util.MachineUpdateReason(), 0, null)
        }

        currentFuelTime = (fuelTime * fuelEfficiency).toInt()
        fuelRemaining = currentFuelTime
        burningFuelType = fuelStack.type

        updateFuelProgressDisplay()
    }

    fun updateFuelState(tickInterval: Int) {
        if (fuelRemaining > 0) {
            fuelRemaining -= tickInterval

            if (fuelRemaining <= 0) {
                fuelRemaining = 0
                burningFuelType = null
                fuelProgressItem.setTotalTimeTicks(null)
                fuelProgressItem.setItem(noFuelDisplay)
            } else {
                fuelProgressItem.setTotalTimeTicks(currentFuelTime)
                fuelProgressItem.setRemainingTimeTicks(fuelRemaining)
                fuelProgressItem.setItem(
                    ItemStackBuilder.of(ItemStack(burningFuelType ?: Material.AIR))
                        .name(Component.translatable("ironfurnaces.gui.fuel_status.name"))
                        .lore(
                            Component.translatable(
                                "ironfurnaces.gui.fuel_status.lore",
                                io.github.pylonmc.rebar.i18n.RebarArgument.of("fuel", ItemStack(burningFuelType!!).displayName())
                            )
                        )
                )
            }
        }

        fuelProgressItem.notifyWindows()
    }

    internal fun getFuelTimeFromServer(stack: ItemStack): Int {
        try {
            val nmsStack = CraftItemStack.asNMSCopy(stack)
            val world = block.world
            val craftWorld = world as org.bukkit.craftbukkit.CraftWorld
            val serverLevel: ServerLevel = craftWorld.handle

            return serverLevel.fuelValues().burnDuration(nmsStack)
        } catch (e: Exception) {
            LOGGER.severe("[${furnaceTier.name}] NMS查询失败: ${e.message}")
            e.printStackTrace()
            return 0
        }
    }

    private fun updateFuelProgressDisplay() {
        fuelProgressItem.setTotalTimeTicks(currentFuelTime)
        fuelProgressItem.setRemainingTimeTicks(currentFuelTime)
        fuelProgressItem.setItem(
            ItemStackBuilder.of(ItemStack(burningFuelType!!))
                .name(Component.translatable("ironfurnaces.gui.fuel_status.name"))
                .lore(
                    Component.translatable(
                        "ironfurnaces.gui.fuel_status.lore",
                        io.github.pylonmc.rebar.i18n.RebarArgument.of("fuel", ItemStack(burningFuelType!!).displayName())
                    )
                )
        )
    }
}

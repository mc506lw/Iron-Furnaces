package top.mc506lw.rebar.ironfurnaces

import io.github.pylonmc.rebar.addon.RebarAddon
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.Material
import java.util.*

class IronFurnaces : JavaPlugin(), RebarAddon {

    companion object {
        lateinit var instance: IronFurnaces
            private set
    }

    override fun onEnable() {
        instance = this

        registerWithRebar()

        IronFurnaceItems.initialize()
        IronFurnaceBlocks.initialize()
        IronFurnacePages.initialise()
    }

    override val javaPlugin: JavaPlugin
        get() = instance

    override val languages: Set<Locale>
        get() = setOf(Locale.CHINESE, Locale.ENGLISH)

    override val material: Material
        get() = Material.FURNACE
}
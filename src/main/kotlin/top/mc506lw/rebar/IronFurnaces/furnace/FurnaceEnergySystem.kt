package top.mc506lw.rebar.ironfurnaces.furnace

interface EnergyConsumer {
    fun consumeEnergy(amount: Double): Boolean
    fun retrieveCurrentEnergy(): Double
    fun getMaxEnergy(): Double
    fun getEnergyPercentage(): Double
}

interface EnergyProducer {
    fun produceEnergy(amount: Double)
    fun retrieveTotalProduced(): Double
    fun getProductionRate(): Double
}

class FurnaceEnergySystem(
    private val block: org.bukkit.block.Block,
    private val maxCapacity: Double = 10000.0
) : EnergyConsumer, EnergyProducer {

    companion object {
        private val LOGGER = java.util.logging.Logger.getLogger("IronFurnace-Energy")
    }

    private var internalCurrentEnergy: Double = 0.0
    private var internalTotalProduced: Double = 0.0
    private var lastProductionRate: Double = 0.0
    private var lastProductionTime: Long = 0L

    override fun consumeEnergy(amount: Double): Boolean {
        if (internalCurrentEnergy >= amount) {
            internalCurrentEnergy -= amount
            return true
        }
        return false
    }

    override fun retrieveCurrentEnergy(): Double = internalCurrentEnergy

    override fun getMaxEnergy(): Double = maxCapacity

    override fun getEnergyPercentage(): Double = if (maxCapacity > 0) (internalCurrentEnergy / maxCapacity) * 100 else 0.0

    override fun produceEnergy(amount: Double) {
        val newEnergy = (internalCurrentEnergy + amount).coerceAtMost(maxCapacity)
        if (newEnergy > internalCurrentEnergy) {
            internalTotalProduced += (newEnergy - internalCurrentEnergy)
        }
        internalCurrentEnergy = newEnergy

        val currentTime = System.currentTimeMillis()
        if (lastProductionTime > 0) {
            val timeDiff = (currentTime - lastProductionTime) / 1000.0
            if (timeDiff > 0) {
                lastProductionRate = amount / timeDiff
            }
        }
        lastProductionTime = currentTime
    }

    override fun retrieveTotalProduced(): Double = internalTotalProduced

    override fun getProductionRate(): Double = lastProductionRate

    fun reset() {
        internalCurrentEnergy = 0.0
        internalTotalProduced = 0.0
        lastProductionRate = 0.0
        lastProductionTime = 0L
    }

    fun convertHeatToEnergy(heatAmount: Double, multiplier: Double = 1.0): Double {
        val energyProduced = heatAmount * 10.0 * multiplier
        produceEnergy(energyProduced)
        LOGGER.fine("[${block.location}] 热能转换: ${String.format("%.1f", heatAmount)} -> ${String.format("%.1f", energyProduced)} FE")
        return energyProduced
    }

    fun isGeneratorMode(): Boolean = internalTotalProduced > 0

    fun getEnergyInfo(): String {
        return "能量: ${String.format("%.1f", internalCurrentEnergy)}/${String.format("%.1f", maxCapacity)} (${String.format("%.1f", getEnergyPercentage())}%)"
    }

    fun getProductionInfo(): String {
        return if (isGeneratorMode()) "发电量: ${String.format("%.1f", internalTotalProduced)} FE | 速率: ${String.format("%.2f", getProductionRate())} FE/t"
        else "未发电"
    }
}

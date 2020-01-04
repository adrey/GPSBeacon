package info.safronoff.gpsbeacon.devicedata

import android.content.Context
import android.os.BatteryManager

interface GetBatteryLevel {
    fun exec(): Int
}

class GetBatteryLevelImpl(private val context: Context) : GetBatteryLevel {
    override fun exec(): Int {
        val bm = context.getSystemService(Context.BATTERY_SERVICE) as BatteryManager
        return bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
    }
}
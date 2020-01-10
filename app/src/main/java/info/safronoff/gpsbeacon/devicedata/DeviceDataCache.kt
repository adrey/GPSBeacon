package info.safronoff.gpsbeacon.devicedata

import android.content.Context
import info.safronoff.gpsbeacon.utils.BaseCache
import info.safronoff.gpsbeacon.utils.BaseCacheImpl

interface DeviceDataCache : BaseCache<DeviceData>

class DeviceDataCacheImpl(context: Context) : DeviceDataCache, BaseCacheImpl<DeviceData>(FILE, context) {
    companion object {
        private const val FILE = "DevDataPrefs"
        private const val DEVICE_DATA = "DEVICE_DATA"
    }

    override fun getArgument(): String {
        return DEVICE_DATA
    }

    override fun getCacheType(): Class<DeviceData> {
        return DeviceData::class.java
    }
}
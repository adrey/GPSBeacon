package info.safronoff.gpsbeacon.devicedata

import android.content.Context
import info.safronoff.gpsbeacon.utils.BaseCache
import info.safronoff.gpsbeacon.utils.BaseCacheImpl

interface DeviceIdCache : BaseCache<String>


class DeviceIdCacheImpl(context: Context) :  BaseCacheImpl<String>(FILE, context), DeviceIdCache {

    companion object {
        private const val FILE = "DevIdPrefs"
        private const val DEVICE_ID = "DEVICE_ID"
    }

    override fun getArgument(): String {
        return DEVICE_ID
    }

    override fun getCacheType(): Class<String> {
        return String::class.java
    }

}
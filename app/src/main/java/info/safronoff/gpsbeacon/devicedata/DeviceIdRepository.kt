package info.safronoff.gpsbeacon.devicedata

import info.safronoff.gpsbeacon.api.API
import io.reactivex.Single

interface DeviceIdRepository {
    fun getId(): Single<String>
}

class DeviceIdRepoImpl(private val deviceIdCache: DeviceIdCache, private val api: API) : DeviceIdRepository {

    override fun getId(): Single<String> {
        val id = deviceIdCache.get()
        return if(id.isNullOrBlank()) {
            api.createDeviceLink().flatMap { devId -> Single.just(devId.id) }.doOnSuccess {
                deviceIdCache.put(it)
            }
        } else {
                Single.just(id)
        }
    }

}
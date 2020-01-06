package info.safronoff.gpsbeacon.devicedata

import io.reactivex.Single

interface GetDeviceId {
    fun exec(): Single<String>
}

class GetDeviceIdImpl(private val deviceIdRepository: DeviceIdRepository) : GetDeviceId {
    override fun exec(): Single<String> {
        return deviceIdRepository.getId()
    }

}
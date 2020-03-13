package info.safronoff.gpsbeacon.devicedata

import io.reactivex.Completable

interface DeleteData  {
    fun exec(id: String): Completable
}

class DeleteDataImpl(private val deviceDataRepository: DeviceDataRepository) : DeleteData {
    override fun exec(id: String): Completable {
        return deviceDataRepository.delete(id)
    }
}
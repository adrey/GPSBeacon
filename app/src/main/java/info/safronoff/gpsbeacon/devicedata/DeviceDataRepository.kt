package info.safronoff.gpsbeacon.devicedata

import info.safronoff.gpsbeacon.api.API
import info.safronoff.gpsbeacon.api.DeviceData
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface DeviceDataRepository {
    fun getUpdates(): Observable<DeviceData>

    fun update(data: DeviceData): Single<DeviceData>
}


class DeviceDataRepoImpl(private val api: API) : DeviceDataRepository {

    private val subject = PublishSubject.create<DeviceData>()

    override fun getUpdates(): Observable<DeviceData> {
        return subject
    }

    override fun update(data: DeviceData): Single<DeviceData> {
        val id = "71dbb0997eec19c77395e75afa115287494e53418bbf2adaca17c0f93e57cf8b"
        return api.updateDevicePosition(id, data).doOnSuccess { subject.onNext(it) }
    }

}
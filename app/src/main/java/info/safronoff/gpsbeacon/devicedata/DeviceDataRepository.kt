package info.safronoff.gpsbeacon.devicedata

import info.safronoff.gpsbeacon.api.API
import info.safronoff.gpsbeacon.api.DeviceData
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

interface DeviceDataRepository {
    fun getUpdates(): Observable<DeviceData>

    fun update(id: String, data: DeviceData): Single<DeviceData>
}


class DeviceDataRepoImpl(private val api: API) : DeviceDataRepository {

    private val subject = PublishSubject.create<DeviceData>()

    override fun getUpdates(): Observable<DeviceData> {
        return subject
    }

    override fun update(id: String, data: DeviceData): Single<DeviceData> {
        return api.updateDevicePosition(id, data).doOnSuccess {
            subject.onNext(it)
        }
    }

}
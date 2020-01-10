package info.safronoff.gpsbeacon.devicedata

import info.safronoff.gpsbeacon.api.API
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface DeviceDataRepository {
    fun getUpdates(): Observable<DeviceData>

    fun update(id: String, data: DeviceData): Single<DeviceData>
}


class DeviceDataRepoImpl(private val api: API, private val cache: DeviceDataCache) : DeviceDataRepository {

    private val subject = BehaviorSubject.create<DeviceData>()

    init {
        cache.get()?.let {
            subject.onNext(it)
        }
    }

    override fun getUpdates(): Observable<DeviceData> {
        return subject
    }

    override fun update(id: String, data: DeviceData): Single<DeviceData> {
        return api.updateDevicePosition(id, data).doOnSuccess {
            cache.put(it)
            subject.onNext(it)
        }
    }

}
package info.safronoff.gpsbeacon.devicedata

import info.safronoff.gpsbeacon.api.API
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface DeviceDataRepository {
    fun getUpdates(): Observable<DeviceData>

    fun update(id: String, data: DeviceData): Single<DeviceData>

    fun delete(id: String): Completable
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

    override fun delete(id: String): Completable {
        return api.clearData(id).doOnComplete {
            cache.clear()
            subject.onNext(DeviceData(0.0, 0.0, 0f, 0, 0))
        }
    }



}
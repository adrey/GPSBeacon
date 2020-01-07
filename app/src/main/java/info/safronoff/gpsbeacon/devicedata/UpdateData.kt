package info.safronoff.gpsbeacon.devicedata

import info.safronoff.gpsbeacon.api.DeviceData
import info.safronoff.gpsbeacon.utils.GetLocation
import info.safronoff.gpsbeacon.utils.GetLocationImpl
import info.safronoff.gpsbeacon.utils.WakeUp
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

interface UpdateData {
    fun exec(id: String): Completable
}

class UpdateDataImpl(private val getLocation: GetLocation,
                     private val deviceDataRepository: DeviceDataRepository,
                     private val getBatteryLevel: GetBatteryLevel,
                     private val wakeUp: WakeUp) : UpdateData {
    override fun exec(id: String): Completable {
        //wakeUp.exec()
        return Completable.fromSingle(
            getLocation.exec()
                .flatMap {
                    Timber.d("Got location ${it.latitude} ${it.longitude}, accuracy ${it.accuracy}")
                    val data = DeviceData(lat=it.latitude,
                        lng = it.longitude,
                        accuracy = it.accuracy,
                        datetime = it.time,
                        battery = getBatteryLevel.exec())
                    deviceDataRepository
                        .update(id, data)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                }
        )

    }
}
package info.safronoff.gpsbeacon.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.safronoff.gpsbeacon.devicedata.DeviceDataRepository
import info.safronoff.gpsbeacon.devicedata.GetDeviceId
import info.safronoff.gpsbeacon.tracking.StartTracking
import info.safronoff.gpsbeacon.utils.GetLocation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import timber.log.Timber

class MainViewModel(
    private val startTracking: StartTracking,
    private val deviceDataRepository: DeviceDataRepository,
    private val getDeviceId: GetDeviceId

) : ViewModel() {

    private var updateDataDisposable: Disposable? = null
    private var startTrackingDisposable: Disposable? = null

    init {
        updateDataDisposable = deviceDataRepository.getUpdates().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                latitude.value = it.lat.toString()
                longitude.value = it.lng.toString()
                accuracy.value = it.accuracy.toString()
                lastUpdate.value = DateTime(it.datetime).toString("dd.MM.yyyy HH:mm:ss")
            }, {
                Timber.e(it.message)
            })
    }
    val latitude = MutableLiveData<String>().apply { value = "" }
    val longitude = MutableLiveData<String>().apply { value = "" }
    val accuracy =  MutableLiveData<String>().apply { value = "" }
    val lastUpdate = MutableLiveData<String>().apply { value = "" }
    val link = MutableLiveData<String>().apply { value = "" }



    override fun onCleared() {
        super.onCleared()
        updateDataDisposable?.let {
            it.dispose()
        }
        startTrackingDisposable?.let {
            it.dispose()
        }
    }

    fun startClick() {
        Timber.d("Start clicked")
        startTrackingDisposable = getDeviceId.exec()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess { link.value = "http://gps-beaconing.appspot.com/link/$it" }
            .flatMap {
                startTracking.exec(it)

            }
            .subscribeOn(Schedulers.io())
            .subscribe({ Timber.d("Tracking started") }, {Timber.d(it.message)})
    }


    fun resetClick() {
        Timber.d("RESET")
    }
}

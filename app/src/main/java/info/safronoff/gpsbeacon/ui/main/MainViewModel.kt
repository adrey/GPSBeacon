package info.safronoff.gpsbeacon.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import info.safronoff.gpsbeacon.devicedata.DeviceDataRepository
import info.safronoff.gpsbeacon.devicedata.GetDeviceId
import info.safronoff.gpsbeacon.devicedata.ShareLink
import info.safronoff.gpsbeacon.tracking.IsTrackingStarted
import info.safronoff.gpsbeacon.tracking.StartTracking
import info.safronoff.gpsbeacon.tracking.StopTracking
import info.safronoff.gpsbeacon.utils.GetLocation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import org.joda.time.DateTime
import timber.log.Timber

class MainViewModel(
    private val startTracking: StartTracking,
    private val deviceDataRepository: DeviceDataRepository,
    private val getDeviceId: GetDeviceId,
    private val isTrackingStarted: IsTrackingStarted,
    private val stopTracking: StopTracking,
    private val shareLink: ShareLink

) : BaseViewModel() {

    init {
        disposables.add(deviceDataRepository.getUpdates().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                latitude.value = it.lat.toString()
                longitude.value = it.lng.toString()
                accuracy.value = it.accuracy.toString()
                lastUpdate.value = DateTime(it.datetime).toString("dd.MM.yyyy HH:mm:ss")
            }, {
                Timber.e(it.message)
            }))
        disposables.add(getDeviceId.exec()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                setLink(it)
            }, {
                Timber.e(it.message)
            }))
        disposables.add(isTrackingStarted.exec()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                isStarted.value = it
            }, {
                Timber.e(it.message)
            }))
    }
    val latitude = MutableLiveData<String>().apply { value = "" }
    val longitude = MutableLiveData<String>().apply { value = "" }
    val accuracy =  MutableLiveData<String>().apply { value = "" }
    val lastUpdate = MutableLiveData<String>().apply { value = "" }
    val link = MutableLiveData<String>().apply { value = "" }
    val isStarted = MutableLiveData<Boolean>().apply { value = false }



    override fun onCleared() {
        super.onCleared()
        disposables.dispose()
    }

    fun startClick() {
        Timber.d("Start clicked")
        disposables.add(getDeviceId.exec()
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSuccess {
                setLink(it)
                startTracking.exec(it)
            }
            .subscribeOn(Schedulers.io())
            .subscribe(
                {
                    Timber.d("Tracking started")
                },
                {Timber.d(it.message)}
            )
        )
    }

    fun stopClick() {
        stopTracking.exec()
    }

    private fun setLink(id: String) {
        link.value = "http://gps-beaconing.appspot.com/link/$id"
    }

    fun shareClick() {
        shareLink.exec(link.value ?: "")
    }


    fun resetClick() {
        Timber.d("RESET")
    }
}

package info.safronoff.gpsbeacon.ui.main

import androidx.lifecycle.ViewModel
import info.safronoff.gpsbeacon.tracking.StartTracking
import info.safronoff.gpsbeacon.utils.GetLocation
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import timber.log.Timber

class MainViewModel(
    private val startTracking: StartTracking,
    private val getLocation: GetLocation
) : ViewModel() {
    var latitude = "55.21"

    fun startClick() {
        Timber.d("Start clicked")
        startTracking
            .exec()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ Timber.d("Tracking started") }, {})
    }


    fun resetClick() {
        Timber.d("RESET")
        getLocation
            .exec()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ Timber.d("Got location, accuracy ${it.accuracy}") }, {})
    }
}

package info.safronoff.gpsbeacon.ui.main

import androidx.lifecycle.ViewModel
import info.safronoff.gpsbeacon.tracking.StartTracking
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

class MainViewModel(private val startTracking: StartTracking) : ViewModel() {
    var latitude = "55.21"

    fun startClick() {
        println("Start clicked")
        startTracking
            .exec()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({println("Tracking started")}, {})
    }
}

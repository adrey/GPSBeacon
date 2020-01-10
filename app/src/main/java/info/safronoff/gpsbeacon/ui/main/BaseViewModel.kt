package info.safronoff.gpsbeacon.ui.main

import androidx.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {
    val disposables = CompositeDisposable()
}
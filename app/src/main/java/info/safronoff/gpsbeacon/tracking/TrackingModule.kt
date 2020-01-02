package info.safronoff.gpsbeacon.tracking

import android.content.Context
import info.safronoff.gpsbeacon.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val trackingModule = module {
    fun provideStartTracking(context: Context): StartTracking {
        return StartTrackingImpl(context)
    }
    single { provideStartTracking(get()) }
    viewModel { MainViewModel(get()) }


}
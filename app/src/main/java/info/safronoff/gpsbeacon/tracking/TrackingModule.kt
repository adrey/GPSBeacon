package info.safronoff.gpsbeacon.tracking

import android.content.Context
import info.safronoff.gpsbeacon.ui.main.MainViewModel
import info.safronoff.gpsbeacon.utils.GetLocation
import info.safronoff.gpsbeacon.utils.GetLocationImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val trackingModule = module {
    fun provideStartTracking(context: Context): StartTracking {
        return StartTrackingImpl(context)
    }

    fun provideGetLocation(context: Context): GetLocation {
        return GetLocationImpl(context)
    }

    single { provideStartTracking(get()) }
    single { provideGetLocation(get()) }
    viewModel { MainViewModel(get(), get()) }


}
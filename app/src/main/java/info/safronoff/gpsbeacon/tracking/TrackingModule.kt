package info.safronoff.gpsbeacon.tracking

import android.content.Context
import info.safronoff.gpsbeacon.ui.main.MainViewModel
import info.safronoff.gpsbeacon.utils.GetLocation
import info.safronoff.gpsbeacon.utils.GetLocationImpl
import info.safronoff.gpsbeacon.utils.WakeUp
import info.safronoff.gpsbeacon.utils.WakeUpImpl
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val trackingModule = module {

    fun provideStartTracking(context: Context): StartTracking {
        return StartTrackingImpl(context)
    }

    fun provideGetLocation(context: Context): GetLocation {
        return GetLocationImpl(context)
    }

    fun provideWakeUp(context: Context): WakeUp {
        return WakeUpImpl(context)
    }

    fun provideTrackingStateRepo(): TrackingStateRepo {
        return TrackingStateRepoImpl()
    }

    fun provideIsStarted(trackingStateRepo: TrackingStateRepo): IsTrackingStarted {
        return IsTrackinStartedImpl(trackingStateRepo)
    }

    fun provideStopTracking(context: Context): StopTracking {
        return StopTrackingImpl(context)
    }

    single { provideWakeUp(get()) }

    single { provideStartTracking(get()) }

    single { provideGetLocation(get()) }

    single { provideStopTracking(get()) }

    single { provideTrackingStateRepo() }

    single { provideIsStarted(get()) }

    viewModel { MainViewModel(get(), get(), get(), get(), get()) }


}
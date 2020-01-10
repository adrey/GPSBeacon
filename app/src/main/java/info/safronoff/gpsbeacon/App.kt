package info.safronoff.gpsbeacon

import android.app.Application
import info.safronoff.gpsbeacon.api.apiModule
import info.safronoff.gpsbeacon.devicedata.deviceDataModule
import info.safronoff.gpsbeacon.tracking.trackingModule
import info.safronoff.gpsbeacon.utils.FileLogTree
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        Timber.plant(
            FileLogTree(
                requireNotNull(applicationContext.getExternalFilesDir(null)),
                "log.txt"
            )
        )
        Timber.plant(Timber.DebugTree())
        startKoin {
            // declare used Android context
            androidContext(this@App)
            // declare modules
            modules(listOf(trackingModule, apiModule, deviceDataModule))
        }
    }
}
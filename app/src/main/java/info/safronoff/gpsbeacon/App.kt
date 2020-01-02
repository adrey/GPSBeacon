package info.safronoff.gpsbeacon

import android.app.Application
import info.safronoff.gpsbeacon.tracking.trackingModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            // declare used Android context
            androidContext(this@App)
            // declare modules
            modules(trackingModule)
        }
    }
}
package info.safronoff.gpsbeacon.tracking

import android.content.Context
import android.content.Intent

/**
 * Start GPS tracking UseCase
 */
interface StopTracking {

    fun exec()
}

class StopTrackingImpl(private val context: Context) : StopTracking {

    override fun exec() {
        val intent = Intent(context, TrackingService::class.java)
        intent.action = TrackingService.STOP_COMMAND
        context.startService(intent)
    }

}
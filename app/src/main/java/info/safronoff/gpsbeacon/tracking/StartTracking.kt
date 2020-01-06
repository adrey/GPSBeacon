package info.safronoff.gpsbeacon.tracking

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import io.reactivex.Single

/**
 * Start GPS tracking UseCase
 */
interface StartTracking {

    /**
     * @return unique link to user position
     */
    fun exec(deviceId: String): Single<String>
}

class StartTrackingImpl(private val context: Context) : StartTracking {

    override fun exec(deviceId: String): Single<String> {
        val intent = Intent(context, TrackingService::class.java)
        intent.action = TrackingService.START_COMMAND
        intent.putExtra(TrackingService.DEVICE_ID_EXTRA, deviceId)
        ContextCompat.startForegroundService(context, intent)
        return Single.just("testLink")
    }

}
package info.safronoff.gpsbeacon.tracking

import android.annotation.SuppressLint
import android.app.*
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import info.safronoff.gpsbeacon.MainActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import info.safronoff.gpsbeacon.devicedata.UpdateData
import io.reactivex.disposables.Disposable
import android.app.PendingIntent
import android.location.Location
import android.widget.Toast
import com.google.android.gms.location.LocationResult
import info.safronoff.gpsbeacon.devicedata.DeleteData


class TrackingService : Service() {

    companion object {
        const val START_COMMAND = "trackingStart"
        const val STOP_COMMAND = "trackingStop"
        const val LOCATION_ALARM_COMMAND = "updateLocationAlarm"
        const val LOCATION_COMMAND = "locationReceived"
        const val DEVICE_ID_EXTRA= "DEVICE_ID"
        private const val REFRESH_INTERVAL_MINUTES = 5L
        private val notificationId = 555
        private val channelId = "trackingNotificationChannel"
        var isStarted = false
    }


    private lateinit var deviceId: String

    private val updateData: UpdateData by inject()

    private val trackingStateRepo: TrackingStateRepo by inject()

    private var disposable: Disposable? = null

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        disposable?.let {
            it.dispose()
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            START_COMMAND -> {
                deviceId = requireNotNull(intent.getStringExtra(DEVICE_ID_EXTRA))
                startService()
            }
            STOP_COMMAND -> {
                stopService()
            }

            LOCATION_ALARM_COMMAND -> updateLocationAndStartAlarm()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun stopService() {
        stopForeground(true)
        isStarted = false
        trackingStateRepo.setStarted(isStarted)
    }

    private fun startService() {
        if (!isStarted) {
            isStarted = true
            trackingStateRepo.setStarted(isStarted)
            updateLocationAndStartAlarm()
            startForeground(notificationId, createNotification())
        }

    }

    @SuppressLint("MissingPermission")
    private fun startPeriodicLoactionUpdates() {
        val locationManager = applicationContext.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val locationIntent = Intent(applicationContext, TrackingService::class.java)
        locationIntent.action = LOCATION_COMMAND
        val locationPendingIntent = PendingIntent.getService(
            applicationContext,
            0,
            locationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, TimeUnit.MINUTES.toMillis(5), 0f, locationPendingIntent)
    }


    @SuppressLint("TimberExceptionLogging")
    private fun updateLocationAndStartAlarm() {
        startService()
        disposable = updateData.exec(deviceId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("data updated")
            }, {
                Timber.e(it.message)
            })
        startLocationAlarm()
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent(applicationContext, MainActivity::class.java)
        val intent = PendingIntent.getActivity(
            applicationContext,
            0,
            notificationIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = createChannel()


        return NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("GPS Beacon")
            .setContentText("Beacon is working")
            .setSmallIcon(info.safronoff.gpsbeacon.R.drawable.ic_beacon)
            .setContentIntent(intent)
            .build()
    }

    private fun createChannel(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelId, NotificationManager.IMPORTANCE_HIGH
            )
            val service =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(channel)
            channelId
        } else {
            ""
        }
    }

    private fun startLocationAlarm() {
        val alarmMgr = applicationContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(applicationContext, TrackingService::class.java).let { intent ->
            intent.action = LOCATION_ALARM_COMMAND
            PendingIntent.getService(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val time = TimeUnit.MINUTES.toMillis(REFRESH_INTERVAL_MINUTES)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            alarmMgr.setExact(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + time,
                alarmIntent
            )
        } else {
            alarmMgr.setExactAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + time,
                alarmIntent
            )
        }
    }

}
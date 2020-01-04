package info.safronoff.gpsbeacon.tracking

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import androidx.core.app.NotificationCompat
import info.safronoff.gpsbeacon.api.API
import info.safronoff.gpsbeacon.api.DeviceData
import info.safronoff.gpsbeacon.utils.GetLocationImpl
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.koin.android.ext.android.inject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import info.safronoff.gpsbeacon.devicedata.UpdateData
import io.reactivex.disposables.Disposable


class TrackingService : Service() {

    companion object {
        const val START_COMMAND = "trackingStart"
        const val LOCATION_COMMAND = "updateLocation"
        private val notificationId = 555
        private val channelId = "trackingNotificationChannel"
        var isStarted = false
    }

    private val updateData: UpdateData by inject()

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
            START_COMMAND -> startService()
            LOCATION_COMMAND -> updateLocationAndStartAlarm()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startService() {
        if (!isStarted) {
            isStarted = true
            updateLocationAndStartAlarm()
            startForeground(notificationId, createNotification())
        }

    }


    private fun updateLocationAndStartAlarm() {
        disposable = updateData.exec()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                Timber.d("data updated")
            }, {
                Timber.e(it)
            })
        startLocationAlarm()
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent()
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
            intent.action = LOCATION_COMMAND
            PendingIntent.getService(
                applicationContext,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
            )
        }
        val time = TimeUnit.MINUTES.toMillis(5)
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
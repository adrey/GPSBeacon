package info.safronoff.gpsbeacon.tracking

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import info.safronoff.gpsbeacon.R

class TrackingService : Service() {

    companion object {
        const val START_COMMAND = "trackingStart"
        private val notificationId = 555
        private val channelId = "trackingNotificationChannel"
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            START_COMMAND -> startService()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun startService() {
        startForeground(notificationId, createNotification())
    }

    private fun createNotification(): Notification {
        val notificationIntent = Intent()
        val intent = PendingIntent.getActivity(applicationContext, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = createChannel()


        return NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle("GPS Beacon")
            .setContentText("Beacon is working")
            .setSmallIcon(R.drawable.ic_beacon)
            .setContentIntent(intent)
            .build()
    }

    private fun createChannel(): String {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId,
                channelId, NotificationManager.IMPORTANCE_HIGH)
            val service = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            service.createNotificationChannel(channel)
            channelId
        } else {
            ""
        }
    }

}
package info.safronoff.gpsbeacon.utils

import android.content.Context
import android.content.Intent
import info.safronoff.gpsbeacon.WakeUpActivity

interface WakeUp {
    fun exec()
}

class WakeUpImpl(private val context: Context): WakeUp {
    override fun exec() {
        val intent = Intent(context, WakeUpActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

}
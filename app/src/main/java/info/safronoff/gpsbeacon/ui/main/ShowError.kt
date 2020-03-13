package info.safronoff.gpsbeacon.ui.main

import android.content.Context
import android.widget.Toast

interface ShowError {
    fun exec(message: String)
}

class ShowErrorImpl(private val context: Context) : ShowError {
    override fun exec(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
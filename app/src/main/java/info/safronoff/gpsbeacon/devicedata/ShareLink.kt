package info.safronoff.gpsbeacon.devicedata

import android.content.Context
import androidx.core.app.ShareCompat
import android.content.Intent
import androidx.core.content.ContextCompat.startActivity



interface ShareLink {
    fun exec(link: String)
}

class ShareLinkImpl(private val context: Context) : ShareLink {
    override fun exec(link: String) {
        val share = Intent(Intent.ACTION_SEND)
        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        share.type = "text/plain"
        share.putExtra(Intent.EXTRA_TEXT, link)
        startActivity(context, share, null)
    }
}
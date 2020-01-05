package info.safronoff.gpsbeacon

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import info.safronoff.gpsbeacon.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null
            && supportFragmentManager.findFragmentByTag(MainFragment.TAG) == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance(), MainFragment.TAG)
                .commitNow()
        }
    }

}

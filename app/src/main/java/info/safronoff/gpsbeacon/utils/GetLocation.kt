package info.safronoff.gpsbeacon.utils

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import io.reactivex.Single
import io.reactivex.subjects.SingleSubject
import timber.log.Timber
import java.util.concurrent.TimeUnit

interface GetLocation {
    fun exec(): Single<Location>
}

class GetLocationImpl(private val context: Context) : GetLocation {

    companion object {
        private const val ACCURACY = 30
    }

    @SuppressLint("MissingPermission")
    override fun exec(): Single<Location> {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val subject = SingleSubject.create<Location>()
        val listener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                Timber.d("Location update, accuracy ${location.accuracy}")
                if (location.accuracy < ACCURACY) {
                    subject.onSuccess(location)
                }
            }

            override fun onStatusChanged(p0: String?, p1: Int, p2: Bundle?) {
            }

            override fun onProviderEnabled(p0: String?) {
            }

            override fun onProviderDisabled(p0: String?) {
            }


        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, listener)

        return subject.timeout(30, TimeUnit.SECONDS)
            .doFinally { locationManager.removeUpdates(listener) }
    }

}
package info.safronoff.gpsbeacon.api

import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.PUT
import retrofit2.http.Path
import java.util.*

interface API {

    @PUT("api/devices/{deviceId}")
    fun updateDevicePosition(@Path("deviceId") deviceId: String,
                             @Body data: DeviceData): Single<DeviceData>
}
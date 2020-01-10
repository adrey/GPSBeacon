package info.safronoff.gpsbeacon.api

import info.safronoff.gpsbeacon.devicedata.DeviceData
import info.safronoff.gpsbeacon.devicedata.DeviceId
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface API {

    @PUT("api/devices/{deviceId}")
    fun updateDevicePosition(@Path("deviceId") deviceId: String,
                             @Body data: DeviceData
    ): Single<DeviceData>


    @POST("api/devices")
    fun createDeviceLink(): Single<DeviceId>
}
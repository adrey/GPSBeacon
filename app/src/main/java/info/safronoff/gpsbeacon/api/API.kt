package info.safronoff.gpsbeacon.api

import info.safronoff.gpsbeacon.devicedata.DeviceData
import info.safronoff.gpsbeacon.devicedata.DeviceId
import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.*

interface API {

    @PUT("api/devices/{deviceId}")
    fun updateDevicePosition(@Path("deviceId") deviceId: String,
                             @Body data: DeviceData
    ): Single<DeviceData>


    @POST("api/devices")
    fun createDeviceLink(): Single<DeviceId>

    @DELETE("api/devices/{deviceId}")
    fun clearData(@Path("deviceId") deviceId: String): Completable
}
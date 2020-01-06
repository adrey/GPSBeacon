package info.safronoff.gpsbeacon.devicedata

import android.content.Context
import info.safronoff.gpsbeacon.api.API
import info.safronoff.gpsbeacon.utils.GetLocation
import org.koin.dsl.module

val updateDataModule = module {
    fun provideUpdateData(getLocation: GetLocation,
                          deviceDataRepository: DeviceDataRepository,
                          getBatteryLevel: GetBatteryLevel): UpdateData {
        return UpdateDataImpl(getLocation = getLocation,
            deviceDataRepository = deviceDataRepository,
            getBatteryLevel = getBatteryLevel)
    }

    fun provideDataRepository(api: API): DeviceDataRepository {
        return DeviceDataRepoImpl(api)
    }

    fun provideBatteryLevel(context: Context): GetBatteryLevel {
        return GetBatteryLevelImpl(context)
    }

    fun provideDeviceIdCache(context: Context): DeviceIdCache {
        return DeviceIdCacheImpl(context)
    }

    fun provideDeviceIdRepo(deviceIdCache: DeviceIdCache, api: API): DeviceIdRepository {
        return DeviceIdRepoImpl(deviceIdCache, api)
    }

    fun provideGetDeviceId(deviceIdRepository: DeviceIdRepository): GetDeviceId {
        return GetDeviceIdImpl(deviceIdRepository)
    }

    single { provideDeviceIdCache(get()) }

    single { provideDeviceIdRepo(get(), get()) }

    single { provideGetDeviceId(get()) }

    single { provideBatteryLevel(get()) }

    single {provideDataRepository(get())}

    single { provideUpdateData(get(), get(), get()) }
}
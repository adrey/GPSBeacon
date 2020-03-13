package info.safronoff.gpsbeacon.devicedata

import android.content.Context
import info.safronoff.gpsbeacon.api.API
import info.safronoff.gpsbeacon.utils.GetLocation
import info.safronoff.gpsbeacon.utils.WakeUp
import info.safronoff.gpsbeacon.utils.WakeUpImpl
import org.koin.dsl.module

val deviceDataModule = module {
    fun provideUpdateData(getLocation: GetLocation,
                          deviceDataRepository: DeviceDataRepository,
                          getBatteryLevel: GetBatteryLevel,
                          wakeUp: WakeUp): UpdateData {
        return UpdateDataImpl(getLocation = getLocation,
            deviceDataRepository = deviceDataRepository,
            getBatteryLevel = getBatteryLevel,
            wakeUp = wakeUp)
    }

    fun provideDeleteData(deviceDataRepository: DeviceDataRepository): DeleteData {
        return DeleteDataImpl(deviceDataRepository = deviceDataRepository)
    }

    fun provideDeviceDataCache(context: Context): DeviceDataCache {
        return DeviceDataCacheImpl(context)
    }

    fun provideDataRepository(api: API, cache: DeviceDataCache): DeviceDataRepository {
        return DeviceDataRepoImpl(api, cache)
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


    single { provideDeviceDataCache(get()) }

    single { provideDeviceIdCache(get()) }

    single { provideDeviceIdRepo(get(), get()) }

    single { provideGetDeviceId(get()) }

    single { provideBatteryLevel(get()) }

    single {provideDataRepository(get(), get())}

    single {provideDeleteData(get())}

    single { provideUpdateData(get(), get(), get(), get()) }
}
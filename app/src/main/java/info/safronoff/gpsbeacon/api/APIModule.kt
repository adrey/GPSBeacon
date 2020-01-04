package info.safronoff.gpsbeacon.api

import com.google.gson.Gson
import info.safronoff.gpsbeacon.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val apiModule = module {
    fun provideInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor()
            .apply {
                level = if (BuildConfig.DEBUG) {
                    HttpLoggingInterceptor.Level.BODY
                } else {
                    HttpLoggingInterceptor.Level.NONE
                }
            }
    }

    fun provideAPI(loggingInterceptor: HttpLoggingInterceptor): API {
        val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://gps-beaconing.appspot.com")
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
        return retrofit.create(API::class.java)
    }

    single { provideInterceptor() }
    single { provideAPI(get()) }
}
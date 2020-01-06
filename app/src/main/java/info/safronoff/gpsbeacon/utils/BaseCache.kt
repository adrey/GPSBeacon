package info.safronoff.gpsbeacon.utils

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.GsonBuilder

interface BaseCache<T> {

    fun put(obj: T)
    fun get(): T?
    fun clear()
}

abstract class BaseCacheImpl<T>(prefFile: String, context: Context) : BaseCache<T> {
    val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)

    protected val prefs: SharedPreferences = EncryptedSharedPreferences.create(
        prefFile,
        masterKeyAlias,
        context,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
    protected val gson = GsonBuilder().enableComplexMapKeySerialization().create()
    abstract fun getArgument(): String
    abstract fun getCacheType(): Class<T>

    override fun put(obj: T) {
        val json = gson.toJson(obj)
        prefs.edit().putString(getArgument(), json).commit()
    }

    override fun get(): T? {
        val data = prefs.getString(getArgument(), null)
        return if(data.isNullOrBlank()) null else gson.fromJson(data, getCacheType())
    }

    override fun clear() {
        prefs.edit().remove(getArgument()).commit()
    }
}

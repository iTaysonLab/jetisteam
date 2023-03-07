package bruhcollective.itaysonlab.jetisteam.controllers

import android.content.Context
import android.content.SharedPreferences
import com.squareup.wire.Message
import com.squareup.wire.ProtoAdapter
import com.tencent.mmkv.MMKV
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.internal.EMPTY_BYTE_ARRAY
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KProperty

@Singleton
class ConfigService @Inject constructor(
    @ApplicationContext applicationContext: Context,
) {
    private val instances: Map<Instance, MMKV>

    enum class Instance {
        Main, Cache
    }

    init {
        MMKV.initialize(applicationContext)

        instances = mapOf(
            Instance.Main to MMKV.defaultMMKV(),
            Instance.Cache to MMKV.mmkvWithID("cache")
        )
    }

    fun get(instance: Instance = Instance.Main): SharedPreferences {
        return instances[instance] ?: error("Instance $instance is not available!")
    }

    fun getBytes(instance: Instance = Instance.Main, key: String, default: ByteArray) = instances[instance]?.getBytes(key, default) ?: default
    fun containsKey(instance: Instance = Instance.Main, key: String) = instances[instance]?.containsKey(key) ?: false
    fun deleteKey(instance: Instance = Instance.Main, key: String) = instances[instance]?.removeValueForKey(key)

    fun put(instance: Instance = Instance.Main, to: String, what: Any) {
        instances[instance]?.apply {
            when (what) {
                is String -> putString(to, what)
                is Int -> putInt(to, what)
                is Long -> putLong(to, what)
                is Boolean -> putBoolean(to, what)
                is ByteArray -> putBytes(to, what)
                else -> error("Not supported type")
            }
        }
    }

    // Abstracts

    inner class LazyStringCfg (private val instance: Instance = Instance.Main, private val key: String, private val ifNotExists: () -> String) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
            return if (containsKey(instance, key)) {
                get(instance).getString(key, null) ?: error("This should not happen")
            } else {
                ifNotExists().also { put(instance, key, it) }
            }
        }
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) { put(instance, key, value) }
    }

    inner class StringCfg (private val instance: Instance = Instance.Main, private val key: String, private val default: String) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>) = get(instance).getString(key, default) ?: default
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) { put(instance, key, value) }
    }

    inner class LongCfg (private val instance: Instance = Instance.Main, private val key: String, private val default: Long) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>) = get(instance).getLong(key, default)
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) { put(instance, key, value) }
    }

    inner class ProtoCfg <T: Message<T, *>> (private val instance: Instance = Instance.Main, private val key: String, private val adapter: ProtoAdapter<T>) {
        // protobuf parsing isn't cheap, so we cache it
        private var _value: T? = null

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            if (_value == null) {
                if (containsKey(instance, key)) {
                    val bytes = getBytes(instance, key, EMPTY_BYTE_ARRAY)
                    if (bytes.isEmpty()) return null
                    _value = adapter.decode(bytes)
                } else {
                    return null
                }
            }

            return _value!!
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            _value = if (value != null) {
                put(instance, key, value.encode())
                value
            } else {
                deleteKey(instance, key)
                null
            }
        }
    }

    fun <T: Message<T, *>> protoCfg(instance: Instance = Instance.Main, key: String, adapter: ProtoAdapter<T>) = ProtoCfg(instance, key, adapter)
}
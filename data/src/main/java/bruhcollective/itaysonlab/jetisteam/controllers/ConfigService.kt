package bruhcollective.itaysonlab.jetisteam.controllers

import android.content.Context
import com.squareup.wire.Message
import com.squareup.wire.ProtoAdapter
import com.tencent.mmkv.MMKV
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KProperty

@Singleton
class ConfigService @Inject constructor(
    @ApplicationContext applicationContext: Context,
) {
    private val instance: MMKV

    init {
        MMKV.initialize(applicationContext)
        instance = MMKV.defaultMMKV()
    }

    fun has(what: String) = instance.containsKey(what)

    fun string(of: String, def: String) = instance.getString(of, def)!!
    fun boolean(of: String, def: Boolean) = instance.getBoolean(of, def)
    fun int(of: String, def: Int) = instance.getInt(of, def)
    fun long(of: String, def: Long) = instance.getLong(of, def)
    fun bytes(of: String, def: ByteArray) = instance.getBytes(of, def)!!
    fun remove(key: String) = instance.removeValueForKey(key)

    fun put(to: String, what: Any) {
        when (what) {
            is String -> instance.putString(to, what)
            is Int -> instance.putInt(to, what)
            is Long -> instance.putLong(to, what)
            is Boolean -> instance.putBoolean(to, what)
            is ByteArray -> instance.putBytes(to, what)
            else -> error("Not supported type")
        }
    }

    // Abstracts

    inner class LazyStringCfg (private val key: String, private val ifNotExists: () -> String) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
            return if (instance.containsKey(key)) {
                instance.getString(key, null) ?: error("This should not happen")
            } else {
                ifNotExists().also { instance.putString(key, it) }
            }
        }
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) { instance.putString(key, value) }
    }

    inner class StringCfg (private val key: String, private val default: String) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>) = instance.getString(key, default) ?: default
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) { instance.putString(key, value) }
    }

    inner class LongCfg (private val key: String, private val default: Long) {
        operator fun getValue(thisRef: Any?, property: KProperty<*>) = instance.getLong(key, default)
        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: Long) { instance.putLong(key, value) }
    }

    // experimental json delegating
    inner class ProtoCfg <T: Message<T, *>> (private val key: String, type: Class<T>) {
        // protobuf parsing isn't cheap, so we cache it
        private var _value: T? = null

        @Suppress("UNCHECKED_CAST")
        private val _parserField = type.getDeclaredField("ADAPTER").get(null) as ProtoAdapter<T>

        operator fun getValue(thisRef: Any?, property: KProperty<*>): T? {
            if (_value == null) {
                if (instance.containsKey(key)) {
                    val bytes = instance.getBytes(key, null)
                    if (bytes == null || bytes.isEmpty()) return null
                    _value = _parserField.decode(bytes)
                } else {
                    return null
                }
            }

            return _value!!
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T?) {
            _value = if (value != null) {
                instance.putBytes(key, value.encode())
                value
            } else {
                instance.removeValueForKey(key)
                null
            }
        }
    }

    inline fun <reified T: Message<T, *>> protoCfg(key: String) = ProtoCfg(key, T::class.java)
}
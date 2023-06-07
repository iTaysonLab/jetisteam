package bruhcollective.itaysonlab.cobalt.core.ksteam

import bruhcollective.itaysonlab.ksteam.database.keyvalue.KeyValueDatabase
import com.tencent.mmkv.MMKV
import okhttp3.internal.EMPTY_BYTE_ARRAY

class KsteamMmkvDatabase(
    private val mmkv: MMKV
): KeyValueDatabase {
    override fun getAllByteArrays(startingFrom: String): List<ByteArray> {
        return mmkv.allKeys()?.filter { it.startsWith(startingFrom, ignoreCase = true) }
            ?.mapNotNull { mmkv.getBytes(it, EMPTY_BYTE_ARRAY) }.orEmpty()
    }

    override fun getByteArray(key: String): ByteArray {
        return mmkv.getBytes(key, EMPTY_BYTE_ARRAY)
    }

    override fun getLong(key: String): Long {
        return mmkv.getLong(key, 0L)
    }

    override fun putByteArray(key: String, value: ByteArray) {
        mmkv.putBytes(key, value)
    }

    override fun putByteArrays(values: Map<String, ByteArray>) {
        values.forEach { (key, array) ->
            mmkv.putBytes(key, array)
        }
    }

    override fun putLong(key: String, value: Long) {
        mmkv.putLong(key, value)
    }
}
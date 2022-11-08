package bruhcollective.itaysonlab.jetisteam.controllers

import com.squareup.moshi.JsonAdapter
import com.squareup.wire.Message
import com.squareup.wire.ProtoAdapter
import okhttp3.internal.EMPTY_BYTE_ARRAY
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

@Singleton
class CacheService @Inject constructor(
    private val configService: ConfigService
) {
    private companion object {
        const val SUFFIX_LAST_CACHED = "lastSavedAt"
    }

    private val cacheInstance get() = configService.get(ConfigService.Instance.Cache)

    suspend fun <T : Message<T, *>> protoEntry(
        key: String,
        adapter: ProtoAdapter<T>,
        maxCacheTime: Duration = 24.hours,
        force: Boolean = false,
        networkFunc: suspend () -> T,
        defaultFunc: () -> T
    ): T {
        val cachedMeta = meta(key) { configService.getBytes(ConfigService.Instance.Cache, it, EMPTY_BYTE_ARRAY) }

        return if (force || cachedMeta.isExpired(maxCacheTime)) {
            network(
                key = key,
                meta = cachedMeta,
                networkFunc = networkFunc,
                defaultFunc = defaultFunc,
                localFunc = adapter::decode,
                cacheWrap = Message<T, *>::encode
            )
        } else if (cachedMeta.data != null) {
            adapter.decode(cachedMeta.data)
        } else {
            defaultFunc()
        }
    }

    suspend fun <T> jsonEntry(
        key: String,
        adapter: JsonAdapter<T>,
        maxCacheTime: Duration = 24.hours,
        force: Boolean = false,
        networkFunc: suspend () -> T,
        defaultFunc: () -> T
    ): T {
        val cachedMeta = meta(key) { cacheInstance.getString(it, null) }

        return if (force || cachedMeta.isExpired(maxCacheTime)) {
            network(
                key = key,
                meta = cachedMeta,
                networkFunc = networkFunc,
                defaultFunc = defaultFunc,
                localFunc = adapter::fromJson,
                cacheWrap = adapter::toJson
            ) ?: defaultFunc()
        } else if (cachedMeta.data != null) {
            adapter.fromJson(cachedMeta.data) ?: defaultFunc()
        } else {
            defaultFunc()
        }
    }

    suspend fun <T: Any> primitiveEntry(
        key: String,
        maxCacheTime: Duration = 24.hours,
        force: Boolean = false,
        networkFunc: suspend () -> T,
        defaultFunc: () -> T,
        cacheFunc: ConfigService.(String) -> T
    ): T {
        val cachedMeta = meta(key, cacheFunc)

        return if (force || cachedMeta.isExpired(maxCacheTime)) {
            network(
                key = key,
                meta = cachedMeta,
                networkFunc = networkFunc,
                defaultFunc = defaultFunc,
                localFunc = { it },
                cacheWrap = { it }
            )
        } else cachedMeta.data ?: defaultFunc()
    }

    private suspend fun <T, Wrap : Any> network(
        key: String,
        meta: EntryMeta<Wrap>,
        networkFunc: suspend () -> T,
        localFunc: (Wrap) -> T,
        defaultFunc: () -> T,
        cacheWrap: (T) -> Wrap
    ): T {
        return try {
            networkFunc().cacheAs(key, cacheWrap)
        } catch (e: Exception) {
            e.printStackTrace()

            if (meta.data != null) {
                localFunc(meta.data)
            } else {
                defaultFunc()
            }
        }
    }

    private fun <T: Any> meta(key: String, dataFetch: ConfigService.(String) -> T?): EntryMeta<T> {
        return EntryMeta(
            time = cacheInstance.getLong(key and SUFFIX_LAST_CACHED, 0),
            data = if (configService.containsKey(ConfigService.Instance.Cache, key)) dataFetch(configService, key) else null
        )
    }

    private fun <T, Wrap : Any> T.cacheAs(key: String, encoder: (T) -> Wrap): T {
        configService.put(ConfigService.Instance.Cache, key, encoder(this))
        configService.put(ConfigService.Instance.Cache, key and SUFFIX_LAST_CACHED, System.currentTimeMillis())
        return this
    }

    private class EntryMeta<T: Any>(
        val time: Long,
        val data: T?
    ) {
        fun isExpired(maxDuration: Duration): Boolean {
            return data == null || System.currentTimeMillis() - time >= maxDuration.inWholeMilliseconds
        }
    }

    private infix fun String.and(str: String) = "$this.$str"
}
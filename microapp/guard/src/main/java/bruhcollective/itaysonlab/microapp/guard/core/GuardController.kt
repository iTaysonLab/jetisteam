package bruhcollective.itaysonlab.microapp.guard.core

import androidx.collection.LongSparseArray
import bruhcollective.itaysonlab.jetisteam.controllers.ConfigService
import bruhcollective.itaysonlab.jetisteam.proto.GuardData
import okhttp3.internal.EMPTY_BYTE_ARRAY
import javax.inject.Inject

class GuardController @Inject constructor(
    private val configService: ConfigService,
    private val sysClock: DefaultClock
) {
    private val lazyInstances = LongSparseArray<GuardInstance>()

    fun getInstance(steamId: Long): GuardInstance? {
        val key = steamIdKey(steamId)
        return if (lazyInstances.containsKey(steamId)) {
            lazyInstances[steamId]!!
        } else if (configService.has(key)) {
            GuardInstance(
                clock = sysClock,
                configuration = GuardData.ADAPTER.decode(configService.bytes(key, EMPTY_BYTE_ARRAY))
            ).also { instance ->
                lazyInstances.put(steamId, instance)
            }
        } else {
            null
        }
    }

    fun createInstance(steamId: Long, configuration: GuardData): GuardInstance {
        return GuardInstance(
            clock = sysClock,
            configuration = configuration
        ).also { instance ->
            configService.put(steamIdKey(steamId), GuardData.ADAPTER.encode(configuration))
            lazyInstances.put(steamId, instance)
        }
    }

    private fun steamIdKey(steamId: Long) = "guard.${steamId}"
}
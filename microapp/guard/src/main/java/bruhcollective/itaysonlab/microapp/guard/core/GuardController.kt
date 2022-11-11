package bruhcollective.itaysonlab.microapp.guard.core

import androidx.collection.LongSparseArray
import bruhcollective.itaysonlab.jetisteam.controllers.ConfigService
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.proto.GuardData
import okhttp3.internal.EMPTY_BYTE_ARRAY
import javax.inject.Inject

class GuardController @Inject constructor(
    private val configService: ConfigService,
    private val sysClock: DefaultClock
) {
    private val lazyInstances = LongSparseArray<GuardInstance>()

    fun getInstance(steamId: SteamID): GuardInstance? {
        val key = steamIdKey(steamId)
        return if (lazyInstances.containsKey(steamId.steamId)) {
            lazyInstances[steamId.steamId]!!
        } else if (configService.containsKey(key = key)) {
            GuardInstance(
                clock = sysClock,
                configuration = GuardData.ADAPTER.decode(configService.getBytes(key = key, default = EMPTY_BYTE_ARRAY))
            ).also { instance ->
                lazyInstances.put(steamId.steamId, instance)
            }
        } else {
            null
        }
    }

    fun createInstance(steamId: SteamID, configuration: GuardData, save: Boolean = true): GuardInstance {
        return GuardInstance(
            clock = sysClock,
            configuration = configuration
        ).also { instance ->
            if (save) {
                saveInstance(steamId, instance)
            }
        }
    }

    fun saveInstance(steamId: SteamID, instance: GuardInstance) {
        configService.put(to = steamIdKey(steamId), what = instance.configurationEncoded)
        lazyInstances.put(steamId.steamId, instance)
    }

    fun deleteInstance(steamId: SteamID) {
        configService.deleteKey(key = steamIdKey(steamId))
        lazyInstances.remove(steamId.steamId)
    }

    private fun steamIdKey(steamId: SteamID) = "guard.${steamId.steamId}"
}
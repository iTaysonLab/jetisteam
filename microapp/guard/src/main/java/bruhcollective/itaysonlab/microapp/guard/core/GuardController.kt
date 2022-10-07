package bruhcollective.itaysonlab.microapp.guard.core

import androidx.collection.LongSparseArray
import bruhcollective.itaysonlab.jetisteam.controllers.ConfigService
import bruhcollective.itaysonlab.jetisteam.controllers.UuidController
import bruhcollective.itaysonlab.jetisteam.proto.GuardData
import okhttp3.internal.EMPTY_BYTE_ARRAY
import javax.inject.Inject

class GuardController @Inject constructor(
    private val configService: ConfigService,
    private val uuidController: UuidController
) {
    private val lazyInstances = LongSparseArray<GuardInstance>()

    fun getInstance(steamId: Long): GuardInstance {
        val key = steamIdKey(steamId)
        return if (lazyInstances.containsKey(steamId)) {
            lazyInstances[steamId]!!
        } else {
            GuardInstance(steamId, if (configService.has(key)) GuardData.ADAPTER.decode(configService.bytes(key, EMPTY_BYTE_ARRAY)) else null).also {
                lazyInstances.put(steamId, it)
            }
        }
    }

    private fun steamIdKey(steamId: Long) = "guard.${steamId}"
}
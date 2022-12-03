package bruhcollective.itaysonlab.microapp.guard.core

import bruhcollective.itaysonlab.jetisteam.controllers.CacheService
import bruhcollective.itaysonlab.jetisteam.controllers.ConfigService
import bruhcollective.itaysonlab.jetisteam.service.ApiService
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ValveClock @Inject constructor(
    private val apiService: ApiService,
    private val cacheService: CacheService,
    private val configService: ConfigService
) {
    suspend fun currentTime() = (System.currentTimeMillis() / 1000L) + provideTimeDifference()
    suspend fun currentTimeMs() = System.currentTimeMillis() + (provideTimeDifference() * 1000L)

    private suspend fun provideTimeDifference(): Long {
        // Log.d("GuardTzNorm", "[TimeDiff] diff: ${configService.getDiff()} | tzId: ${configService.getTzId()} (device: ${TimeZone.getDefault().id})")
        return cacheService.primitiveEntry(
            key = "guard.time.diff",
            force = configService.getTzId() != TimeZone.getDefault().id,
            networkFunc = this::requestServerTime,
            defaultFunc = { return@primitiveEntry 0L },
            cacheFunc = { getDiff() }
        ).also { configService.put(ConfigService.Instance.Main, "guard.time.tzId", TimeZone.getDefault().id) }
    }

    private fun ConfigService.getTzId() = get().getString("guard.time.tzId", "")
    private fun ConfigService.getDiff() = get().getLong("guard.time.diff", 0L)

    private suspend fun requestServerTime(): Long {
        return apiService.queryTime("0").response.let { data ->
            if (data.allowCorrection == true) {
                data.serverTime - (System.currentTimeMillis() / 1000L)
            } else {
                0L
            }
        }
    }
}
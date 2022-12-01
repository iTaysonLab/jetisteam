package bruhcollective.itaysonlab.microapp.guard.core

import bruhcollective.itaysonlab.jetisteam.service.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GuardClockNormalizer @Inject constructor(
    private val apiService: ApiService
) {
    private var qtResponse: ApiService.QueryTimeData? = null
    private var qtDiff: Long = 0

    suspend fun normalize(ms: Long): Long {
        if (qtResponse == null) loadQt()
        return (ms / 1000L) + qtDiff
    }

    private suspend fun loadQt() {
        qtResponse = apiService.queryTime("0").response

        qtDiff = if (qtResponse?.allowCorrection == true) {
            qtResponse!!.serverTime - (System.currentTimeMillis() / 1000L)
        } else {
            0L
        }
    }
}
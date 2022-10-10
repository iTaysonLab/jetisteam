package bruhcollective.itaysonlab.jetisteam.usecases

import bruhcollective.itaysonlab.jetisteam.controllers.ConfigService
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.repository.UserAccountRepository
import javax.inject.Inject
import kotlin.time.Duration.Companion.hours

class GetUserCountry @Inject constructor(
    private val userAccountRepository: UserAccountRepository,
    private val configService: ConfigService
) {
    suspend operator fun invoke(steamId: SteamID, force: Boolean = false): String {
        val key = key(steamId)

        val cachedTime = configService.long("$key.lastSavedAt", 0)
        val cachedData = configService.string(key, "")

        return if (force || System.currentTimeMillis() - cachedTime >= 24.hours.inWholeMilliseconds || cachedData.isEmpty()) {
            try {
                userAccountRepository.getUserCountry(steamId.steamId).also { country ->
                    configService.put(key, country)
                    configService.put("$key.lastSavedAt", System.currentTimeMillis())
                }
            } catch (e: Exception) {
                // probably a network one, so don't crash
                cachedData.ifEmpty { "US" }
            }
        } else {
            cachedData
        }
    }

    private fun key(steamId: SteamID) = "steam.account.${steamId.steamId}.country"
}
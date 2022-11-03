package bruhcollective.itaysonlab.jetisteam.usecases

import bruhcollective.itaysonlab.jetisteam.controllers.CacheService
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.repository.UserAccountRepository
import javax.inject.Inject

class GetUserCountry @Inject constructor(
    private val userAccountRepository: UserAccountRepository,
    private val cacheService: CacheService
) {
    suspend operator fun invoke(steamId: SteamID, force: Boolean = false): String {
        return cacheService.primitiveEntry(
            key = key(steamId),
            force = force,
            networkFunc = {
                userAccountRepository.getUserCountry(steamId.steamId)
            },
            defaultFunc = { "US" },
            cacheFunc = { internalKey ->
                string(internalKey, "US")
            }
        )
    }

    private fun key(steamId: SteamID) = "steam.account.${steamId.steamId}.country"
}
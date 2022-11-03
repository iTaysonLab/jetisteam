package bruhcollective.itaysonlab.jetisteam.controllers

import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.repository.UserAccountRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocaleService @Inject constructor(
    private val userAccountRepository: UserAccountRepository,
    private val cacheService: CacheService,
    private val steamSessionController: SteamSessionController
) {
    private companion object {
        const val DEFAULT_LANGUAGE = "english"
        const val DEFAULT_COUNTRY_CODE = "US"
    }

    // TODO: dynamic
    val language = DEFAULT_LANGUAGE

    suspend fun myCountry(force: Boolean = false) = countryFor(steamId = steamSessionController.steamId(), force = force)

    suspend fun countryFor(steamId: SteamID, force: Boolean = false): String {
        return cacheService.primitiveEntry(
            key = keyCountry(steamId),
            force = force,
            networkFunc = {
                userAccountRepository.getUserCountry(steamId.steamId)
            },
            defaultFunc = { DEFAULT_COUNTRY_CODE },
            cacheFunc = { internalKey ->
                string(internalKey, DEFAULT_COUNTRY_CODE)
            }
        )
    }

    private fun keyCountry(steamId: SteamID) = "steam.account.${steamId.steamId}.country"
}
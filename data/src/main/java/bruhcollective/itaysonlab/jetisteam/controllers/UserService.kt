package bruhcollective.itaysonlab.jetisteam.controllers

import bruhcollective.itaysonlab.jetisteam.models.Player
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.service.ApiService
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserService @Inject constructor(
    private val apiService: ApiService
) {
    suspend fun resolveUsers(steamIds: List<Long>): Map<SteamID, Player> {
        return apiService.resolvePlayers(steamIds.joinToString(",")).players.associateBy { it.steamId }
    }

    private fun key(steamId: Long) = "steam.profiles.$steamId"
}
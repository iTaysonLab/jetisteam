package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.service.GameService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GameRepository @Inject constructor(
    private val gameService: GameService
) {
    suspend fun getGameDetails(appId: String) = gameService.getGameDetails(appId)
}
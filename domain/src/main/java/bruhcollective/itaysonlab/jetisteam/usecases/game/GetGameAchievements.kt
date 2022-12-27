package bruhcollective.itaysonlab.jetisteam.usecases.game

import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.repository.ProfileRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import steam.player.CPlayer_GetGameAchievements_Response
import steam.player.CPlayer_GetTopAchievementsForGames_Response
import javax.inject.Inject

class GetGameAchievements @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val steamSessionController: SteamSessionController
) {
    suspend operator fun invoke(
        appId: Int
    ): AchievementsPacked = withContext(Dispatchers.Default) {
        val globalAchievements = profileRepository.getGameAchievements(appId)
        val myAchievements = profileRepository.getTopGameAchievements(steamSessionController.steamId().steamId, listOf(appId), maxAchievements = globalAchievements.size).values.firstOrNull()?.achievements?.associateBy { it.name.orEmpty() } ?: emptyMap()

        return@withContext AchievementsPacked(
            global = globalAchievements.sortedByDescending {
                myAchievements.keys.contains(it.localized_name)
            },
            unlocked = myAchievements
        )
    }

    class AchievementsPacked(
        val global: List<CPlayer_GetGameAchievements_Response.Achievement>,
        val unlocked: Map<String, CPlayer_GetTopAchievementsForGames_Response.Achievement>
    )
}
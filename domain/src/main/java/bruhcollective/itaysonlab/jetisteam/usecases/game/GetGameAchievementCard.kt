package bruhcollective.itaysonlab.jetisteam.usecases.game

import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.repository.ProfileRepository
import steam.player.CPlayer_GetAchievementsProgress_Response_AchievementProgress
import javax.inject.Inject

class GetGameAchievementCard @Inject constructor(
    private val profileRepository: ProfileRepository,
) {
    suspend operator fun invoke(
        steamId: SteamID,
        appId: Int
    ): CPlayer_GetAchievementsProgress_Response_AchievementProgress {
        return profileRepository.getAchievementsProgress(
            steamId.steamId,
            listOf(appId)
        ).achievement_progress.first()
    }
}
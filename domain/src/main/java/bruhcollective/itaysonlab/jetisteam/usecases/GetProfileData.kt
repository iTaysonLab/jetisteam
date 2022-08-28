package bruhcollective.itaysonlab.jetisteam.usecases

import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.mappers.ProfileCustomization
import bruhcollective.itaysonlab.jetisteam.mappers.ProfileEquipment
import bruhcollective.itaysonlab.jetisteam.mappers.ProfileSummary
import bruhcollective.itaysonlab.jetisteam.models.Miniprofile
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.repository.ProfileRepository
import bruhcollective.itaysonlab.jetisteam.service.MiniprofileService
import steam.player.CPlayer_GetAchievementsProgress_Response
import steam.player.CPlayer_GetAchievementsProgress_Response_AchievementProgress
import steam.player.CPlayer_GetOwnedGames_Response_Game
import steam.player.EProfileCustomizationType
import javax.inject.Inject

class GetProfileData @Inject constructor(
    private val steamSessionController: SteamSessionController,
    private val profileRepository: ProfileRepository,
    private val miniprofileService: MiniprofileService,
) {
    suspend operator fun invoke(
        steamid: SteamID = steamSessionController.steamId()
    ): ProfileData {
        val summary = if (steamSessionController.isMe(steamid)) {
            ProfileSummary(profileRepository.getMyProfileSummary())
        } else {
            null
        }

        val customization =
            ProfileCustomization(profileRepository.getProfileCustomization(steamid.steamId))
        val ownedGames = profileRepository.getOwnedGames(steamid.steamId).gamesList

        val inSlotsWithAchievements = customization.profileCustomizationEntries
            .filter {
                it.customizationType in listOf(
                    EProfileCustomizationType.k_EProfileCustomizationTypeAchievements,
                    EProfileCustomizationType.k_EProfileCustomizationTypeAchievementsCompletionist,
                    EProfileCustomizationType.k_EProfileCustomizationTypeRareAchievementShowcase,
                    EProfileCustomizationType.k_EProfileCustomizationTypeFavoriteGame,
                )
            }
            .map { it.slots }
            .flatten()
            .map { it.appId }

        val recentGames = ownedGames
            .sortedByDescending { it.rtimeLastPlayed }
            .take(3)

        return ProfileData(
            miniProfile = miniprofileService.getMiniprofile(steamid.accountId),
            summary = summary,
            equipment = ProfileEquipment(profileRepository.getProfileItems(steamid.steamId)),
            customization = customization,
            ownedGames = ownedGames.associateBy { it.appid },
            recentActivityPast2Weeks = recentGames.sumOf { it.playtime2Weeks },
            recentActivityGames = recentGames,
            achievementsProgress = profileRepository.getAchievementsProgress(
                steamid.steamId,
                inSlotsWithAchievements + recentGames.map { it.appid }
            ).achievementProgressList.associateBy { it.appid }
        )
    }

    class ProfileData(
        val miniProfile: Miniprofile,
        val summary: ProfileSummary?,
        val equipment: ProfileEquipment,
        val customization: ProfileCustomization,
        val ownedGames: Map<Int, CPlayer_GetOwnedGames_Response_Game>,
        val recentActivityPast2Weeks: Int,
        val recentActivityGames: List<CPlayer_GetOwnedGames_Response_Game>,
        val achievementsProgress: Map<Int, CPlayer_GetAchievementsProgress_Response_AchievementProgress>
    )
}
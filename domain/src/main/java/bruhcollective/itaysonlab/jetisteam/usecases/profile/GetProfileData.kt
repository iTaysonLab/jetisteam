package bruhcollective.itaysonlab.jetisteam.usecases.profile

import bruhcollective.itaysonlab.jetisteam.controllers.CdnController
import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.controllers.UserService
import bruhcollective.itaysonlab.jetisteam.mappers.ProfileCustomization
import bruhcollective.itaysonlab.jetisteam.mappers.ProfileEquipment
import bruhcollective.itaysonlab.jetisteam.mappers.ProfileSummary
import bruhcollective.itaysonlab.jetisteam.models.Player
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.repository.ProfileRepository
import bruhcollective.itaysonlab.jetisteam.repository.StoreRepository
import steam.common.StoreBrowseItemDataRequest
import steam.common.StoreItemID
import steam.player.CPlayer_GetAchievementsProgress_Response_AchievementProgress
import steam.player.CPlayer_GetOwnedGames_Response_Game
import steam.player.EProfileCustomizationType
import javax.inject.Inject

class GetProfileData @Inject constructor(
    private val steamSessionController: SteamSessionController,
    private val profileRepository: ProfileRepository,
    private val userService: UserService,
    private val cdnController: CdnController,
    private val storeRepository: StoreRepository
) {
    suspend operator fun invoke(
        steamid: SteamID = steamSessionController.steamId()
    ): ProfileData {
        val summary = if (steamSessionController.isMe(steamid)) {
            ProfileSummary(profileRepository.getMyProfileSummary())
        } else {
            null
        }

        val customization = ProfileCustomization(profileRepository.getProfileCustomization(steamid.steamId))
        val ownedGames = profileRepository.getOwnedGames(steamid.steamId, includeFreeToPlay = true).games
        val assocOwnedGames = ownedGames.associateBy { it.appid!! }

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
            .sortedByDescending { it.rtime_last_played }
            .take(3)

        val playerProfile = userService.resolveUsers(listOf(steamid.steamId)).values.first()

        val notOwnedButSpecifiedGames = inSlotsWithAchievements.filterNot { appId ->
            assocOwnedGames.containsKey(appId)
        }

        return ProfileData(
            steamID = steamid,
            playerProfile = playerProfile,
            summary = summary,
            equipment = ProfileEquipment(profileRepository.getProfileItems(steamid.steamId)),
            customization = customization,
            ownedGames = assocOwnedGames,
            recentActivityPast2Weeks = recentGames.sumOf { it.playtime_2weeks ?: 0 },
            recentActivityGames = recentGames,
            achievementsProgress = profileRepository.getAchievementsProgress(
                steamid.steamId,
                inSlotsWithAchievements + recentGames.map { it.appid!! }
            ).achievement_progress.associateBy { it.appid!! },
            otherAppsInfo = if (notOwnedButSpecifiedGames.isNotEmpty()) {
                storeRepository.getItems(
                    ids = notOwnedButSpecifiedGames.map { StoreItemID(appid = it) },
                    dataRequest = StoreBrowseItemDataRequest()
                ).store_items.map {
                    it.appid!! to it.name.orEmpty()
                }.associateBy { it.first }
            } else {
                emptyMap()
            }
        )
    }

    class ProfileData(
        val steamID: SteamID,
        val playerProfile: Player,
        val summary: ProfileSummary?,
        val equipment: ProfileEquipment,
        val customization: ProfileCustomization,
        val ownedGames: Map<Int, CPlayer_GetOwnedGames_Response_Game>,
        val recentActivityPast2Weeks: Int,
        val recentActivityGames: List<CPlayer_GetOwnedGames_Response_Game>,
        val achievementsProgress: Map<Int, CPlayer_GetAchievementsProgress_Response_AchievementProgress>,
        val otherAppsInfo: Map<Int, Pair<Int, String>>
    )
}
package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.controllers.LocaleService
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import steam.mobileapp.CMobileApp_GetMobileSummary_Request
import steam.mobileapp.MobileApp
import steam.player.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProfileRepository @Inject constructor(
    steamRpcClient: SteamRpcClient,
    private val localeService: LocaleService
) {
    private val playerStub = steamRpcClient.create<Player>()
    private val mobileAppStub = steamRpcClient.create<MobileApp>()

    suspend fun getMyProfileSummary() = mobileAppStub.GetMobileSummary(CMobileApp_GetMobileSummary_Request())

    suspend fun getProfileItems(steamid: Long) = playerStub.GetProfileItemsEquipped(
        CPlayer_GetProfileItemsEquipped_Request(
            steamid = steamid,
            language = localeService.language
        )
    )

    suspend fun getProfileItemsOwned(filter: ECommunityItemClass) = playerStub.GetProfileItemsOwned(
        CPlayer_GetProfileItemsOwned_Request(
            language = localeService.language,
            filters = listOf(filter)
        )
    )

    suspend fun getProfileCustomization(steamid: Long) = playerStub.GetProfileCustomization(
        CPlayer_GetProfileCustomization_Request(
            steamid = steamid,
            include_inactive_customizations = false,
            include_purchased_customizations = false
        )
    )

    suspend fun getOwnedGames(steamid: Long, includeFreeToPlay: Boolean = false) = withContext(Dispatchers.IO) {
        playerStub.GetOwnedGames(
            CPlayer_GetOwnedGames_Request(
                steamid = steamid,
                language = localeService.language,
                skip_unvetted_apps = false,
                include_appinfo = true,
                include_extended_appinfo = true,
                include_free_sub = includeFreeToPlay,
                include_played_free_games = includeFreeToPlay
            )
        )
    }

    suspend fun getAchievementsProgress(steamid: Long, appids: List<Int>) = withContext(Dispatchers.IO) {
        playerStub.GetAchievementsProgress(
            CPlayer_GetAchievementsProgress_Request(
                steamid = steamid,
                language = localeService.language,
                appids = appids
            )
        )
    }

    suspend fun getGameAchievements(appid: Int) = playerStub.GetGameAchievements(
        CPlayer_GetGameAchievements_Request(
            appid = appid,
            language = localeService.language,
        )
    ).achievements

    suspend fun getTopGameAchievements(steamid: Long, appids: List<Int>, maxAchievements: Int = 8) = playerStub.GetTopAchievementsForGames(
        CPlayer_GetTopAchievementsForGames_Request(
            appids = appids,
            language = localeService.language,
            steamid = steamid,
            max_achievements = maxAchievements
        )
    ).games.filter { it.appid != null }.associateBy { it.appid ?: 0 }

    suspend fun getEquippedProfileItem(steamid: Long, clazz: ECommunityItemClass) = when (clazz) {
        ECommunityItemClass.k_ECommunityItemClass_ProfileBackground -> {
            playerStub.GetProfileBackground(CPlayer_GetProfileBackground_Request(steamid, localeService.language)).profile_background
        }

        ECommunityItemClass.k_ECommunityItemClass_MiniProfileBackground -> {
            playerStub.GetMiniProfileBackground(CPlayer_GetMiniProfileBackground_Request(steamid, localeService.language)).profile_background
        }

        ECommunityItemClass.k_ECommunityItemClass_AvatarFrame -> {
            playerStub.GetAvatarFrame(CPlayer_GetAvatarFrame_Request(steamid, localeService.language)).avatar_frame
        }

        ECommunityItemClass.k_ECommunityItemClass_AnimatedAvatar -> {
            playerStub.GetAnimatedAvatar(CPlayer_GetAnimatedAvatar_Request(steamid, localeService.language)).avatar
        }

        else -> error("Type $clazz is not requestable separately")
    }

    suspend fun setEquippedProfileItem(itemId: Long, clazz: ECommunityItemClass) = when (clazz) {
        ECommunityItemClass.k_ECommunityItemClass_ProfileBackground -> {
            playerStub.SetProfileBackground(CPlayer_SetProfileBackground_Request(itemId))
        }

        ECommunityItemClass.k_ECommunityItemClass_MiniProfileBackground -> {
            playerStub.SetMiniProfileBackground(CPlayer_SetMiniProfileBackground_Request(itemId))
        }

        ECommunityItemClass.k_ECommunityItemClass_AvatarFrame -> {
            playerStub.SetAvatarFrame(CPlayer_SetAvatarFrame_Request(itemId))
        }

        ECommunityItemClass.k_ECommunityItemClass_AnimatedAvatar -> {
            playerStub.SetAnimatedAvatar(CPlayer_SetAnimatedAvatar_Request(itemId))
        }

        else -> error("Type $clazz is not settable in generic type")
    }
}
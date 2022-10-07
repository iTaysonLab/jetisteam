package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import bruhcollective.itaysonlab.jetisteam.util.LanguageUtil
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
) {
    private val playerStub = steamRpcClient.create<Player>()
    private val mobileAppStub = steamRpcClient.create<MobileApp>()

    suspend fun getMyProfileSummary() = mobileAppStub.GetMobileSummary(CMobileApp_GetMobileSummary_Request())

    suspend fun getProfileItems(steamid: Long) = playerStub.GetProfileItemsEquipped(
        CPlayer_GetProfileItemsEquipped_Request(
            steamid = steamid,
            language = LanguageUtil.currentLanguage
        )
    )

    suspend fun getProfileCustomization(steamid: Long) = playerStub.GetProfileCustomization(
        CPlayer_GetProfileCustomization_Request(
            steamid = steamid,
            include_inactive_customizations = false,
            include_purchased_customizations = false
        )
    )

    suspend fun getOwnedGames(steamid: Long) = withContext(Dispatchers.IO) {
        playerStub.GetOwnedGames(
            CPlayer_GetOwnedGames_Request(
                steamid = steamid,
                language = LanguageUtil.currentLanguage,
                skip_unvetted_apps = true,
                include_appinfo = true,
                include_extended_appinfo = true,
                include_free_sub = false,
                include_played_free_games = false
            )
        )
    }

    suspend fun getAchievementsProgress(steamid: Long, appids: List<Int>) = withContext(Dispatchers.IO) {
        playerStub.GetAchievementsProgress(
            CPlayer_GetAchievementsProgress_Request(
                steamid = steamid,
                language = LanguageUtil.currentLanguage,
                appids = appids
            )
        )
    }
}
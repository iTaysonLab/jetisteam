package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcChannel
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcController
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
    steamRpcChannel: SteamRpcChannel,
    private val steamSessionController: SteamSessionController
) {
    private val playerStub = Player.newBlockingStub(steamRpcChannel)
    private val mobileAppStub = MobileApp.newBlockingStub(steamRpcChannel)

    suspend fun getMyProfileSummary() = withContext(Dispatchers.IO) {
        mobileAppStub.getMobileSummary(
            SteamRpcController(post = true), CMobileApp_GetMobileSummary_Request.newBuilder().build()
        )
    }

    suspend fun getProfileItems(steamid: Long) = withContext(Dispatchers.IO) {
        playerStub.getProfileItemsEquipped(
            SteamRpcController(), CPlayer_GetProfileItemsEquipped_Request.newBuilder()
                .setSteamid(steamid)
                .setLanguage(LanguageUtil.getCurrentLanguage())
                .build()
        )
    }

    suspend fun getProfileCustomization(steamid: Long) = withContext(Dispatchers.IO) {
        playerStub.getProfileCustomization(
            SteamRpcController(), CPlayer_GetProfileCustomization_Request.newBuilder()
                .setSteamid(steamid)
                .setIncludeInactiveCustomizations(false)
                .setIncludePurchasedCustomizations(false)
                .build()
        )
    }

    suspend fun getOwnedGames(steamid: Long) = withContext(Dispatchers.IO) {
        playerStub.getOwnedGames(
            SteamRpcController(), CPlayer_GetOwnedGames_Request.newBuilder()
                .setSteamid(steamid)
                .setSkipUnvettedApps(true)
                .setIncludeAppinfo(true)
                .setIncludeExtendedAppinfo(true)
                .setIncludePlayedFreeGames(true)
                .setLanguage(LanguageUtil.getCurrentLanguage())
                .build()
        )
    }

    suspend fun getAchievementsProgress(steamid: Long, appids: List<Int>) = withContext(Dispatchers.IO) {
        playerStub.getAchievementsProgress(
            SteamRpcController(post = true), CPlayer_GetAchievementsProgress_Request.newBuilder()
                .setSteamid(steamid)
                .addAllAppids(appids)
                .setLanguage(LanguageUtil.getCurrentLanguage())
                .build()
        )
    }
}
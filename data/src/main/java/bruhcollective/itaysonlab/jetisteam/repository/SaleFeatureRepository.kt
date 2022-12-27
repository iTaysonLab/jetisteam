package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import steam.salefeature.CSaleFeature_GetFriendsSharedYearInReview_Request
import steam.salefeature.CSaleFeature_GetUserYearAchievements_Request
import steam.salefeature.CSaleFeature_GetUserYearInReview_Request
import steam.salefeature.SaleFeature
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SaleFeatureRepository @Inject constructor(
    steamRpcClient: SteamRpcClient
) {
    private val stub = steamRpcClient.create<SaleFeature>()

    suspend fun getYearInReview(
        steamID: SteamID,
    ) = stub.GetUserYearInReview(
        CSaleFeature_GetUserYearInReview_Request(
            steamid = steamID.steamId,
            force_regenerate = false,
            access_source = 0,
            year = 2022
        )
    )

    suspend fun getYearInReviewAchievements(
        steamID: SteamID,
        appids: List<Int>
    ) = stub.GetUserYearAchievements(
        CSaleFeature_GetUserYearAchievements_Request(
            steamid = steamID.steamId,
            year = 2022,
            appids = appids,
            total_only = false
        )
    )

    suspend fun getOtherUsersWithYIR(
        steamID: SteamID,
    ) = stub.GetFriendsSharedYearInReview(
        CSaleFeature_GetFriendsSharedYearInReview_Request(
            steamid = steamID.steamId,
            year = 2022,
            return_private = true
        )
    )
}
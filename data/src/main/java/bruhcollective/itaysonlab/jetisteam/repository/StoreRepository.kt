package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcChannel
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcController
import bruhcollective.itaysonlab.jetisteam.util.LanguageUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import steam.common.CStoreBrowse_GetItems_Request
import steam.common.StoreBrowseContext
import steam.common.StoreBrowseItemDataRequest
import steam.common.StoreItemID
import steam.friendslist.CFriendsList_GetFriendsList_Request
import steam.friendslist.FriendsList
import steam.storebrowse.StoreBrowse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepository @Inject constructor(
    steamRpcChannel: SteamRpcChannel
) {
    private val browseStub = StoreBrowse.newBlockingStub(steamRpcChannel)

    suspend fun getItems(
        ids: List<StoreItemID>
    ) = withContext(Dispatchers.IO) {
        browseStub.getItems(
            SteamRpcController(), CStoreBrowse_GetItems_Request.newBuilder()
                .addAllIds(ids)
                .setContext(StoreBrowseContext.newBuilder()
                    .setLanguage(LanguageUtil.currentLanguage)
                    .setCountryCode(LanguageUtil.currentRegion)
                    .build())
                .setDataRequest(StoreBrowseItemDataRequest.newBuilder()
                    .setIncludeTagCount(0)
                    .setIncludeAssets(true)
                    .build())
                .build()
        )
    }
}
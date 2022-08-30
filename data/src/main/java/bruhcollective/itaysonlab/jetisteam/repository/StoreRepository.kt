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
import steam.store.CStore_GetLocalizedNameForTags_Request
import steam.store.Store
import steam.storebrowse.StoreBrowse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepository @Inject constructor(
    steamRpcChannel: SteamRpcChannel
) {
    private val stub = Store.newBlockingStub(steamRpcChannel)
    private val browseStub = StoreBrowse.newBlockingStub(steamRpcChannel)

    suspend fun getItems(
        ids: List<StoreItemID>,
        dataRequest: StoreBrowseItemDataRequest
    ) = withContext(Dispatchers.IO) {
        browseStub.getItems(
            SteamRpcController(), CStoreBrowse_GetItems_Request.newBuilder()
                .addAllIds(ids)
                .setContext(StoreBrowseContext.newBuilder()
                    .setLanguage(LanguageUtil.currentLanguage)
                    .setCountryCode(LanguageUtil.currentRegion)
                    .build())
                .setDataRequest(dataRequest)
                .build()
        )
    }

    suspend fun getLocalizedTags(
        ids: List<Int>
    ) = withContext(Dispatchers.IO) {
        stub.getLocalizedNameForTags(
            SteamRpcController(), CStore_GetLocalizedNameForTags_Request.newBuilder()
                .setLanguage(LanguageUtil.currentLanguage)
                .addAllTagids(ids)
                .build()
        ).tagsList.associate { it.tagid to it.name }
    }
}
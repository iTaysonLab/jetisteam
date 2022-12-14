package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.controllers.LocaleService
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import steam.common.CStoreBrowse_GetItems_Request
import steam.common.StoreBrowseContext
import steam.common.StoreBrowseItemDataRequest
import steam.common.StoreItemID
import steam.store.CStore_GetLocalizedNameForTags_Request
import steam.store.Store
import steam.storebrowse.StoreBrowse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StoreRepository @Inject constructor(
    steamRpcClient: SteamRpcClient,
    private val localeService: LocaleService,
) {
    private val stub = steamRpcClient.create<Store>()
    private val browseStub = steamRpcClient.create<StoreBrowse>()

    suspend fun getItems(
        ids: List<StoreItemID>,
        dataRequest: StoreBrowseItemDataRequest
    ) = browseStub.GetItems(
        CStoreBrowse_GetItems_Request(
            ids = ids,
            data_request = dataRequest,
            context = StoreBrowseContext(
                language = localeService.language,
                country_code = localeService.myCountry()
            ),
        )
    )

    suspend fun getLocalizedTags(
        ids: List<Int>
    ) = stub.GetLocalizedNameForTags(
        CStore_GetLocalizedNameForTags_Request(
            tagids = ids,
            language = localeService.language
        )
    ).tags.associate { it.tagid to it.name }
}
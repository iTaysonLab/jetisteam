package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcChannel
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import steam.econ.CEcon_GetInventoryItemsWithDescriptions_Request
import steam.econ.CEcon_GetInventoryItemsWithDescriptions_Request_FilterOptions
import steam.econ.Econ
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EconRepository @Inject constructor(
    steamRpcChannel: SteamRpcChannel
) {
    private val stub = Econ.newBlockingStub(steamRpcChannel)

    suspend fun getInventoryItemsWithDescriptions(
        steamid: Long,
        appid: Int,
        contextid: Long,
        getDescriptions: Boolean,
        assetids: List<Long>
    ) = withContext(Dispatchers.IO) {
        stub.getInventoryItemsWithDescriptions(
            SteamRpcController(), CEcon_GetInventoryItemsWithDescriptions_Request.newBuilder()
                .setSteamid(steamid)
                .setAppid(appid)
                .setContextid(contextid)
                .setGetDescriptions(getDescriptions)
                .setFilters(CEcon_GetInventoryItemsWithDescriptions_Request_FilterOptions.newBuilder().addAllAssetids(assetids).build())
                .build()
        )
    }
}
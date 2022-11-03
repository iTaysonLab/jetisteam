package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.controllers.LocaleService
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import steam.econ.CEcon_GetInventoryItemsWithDescriptions_Request
import steam.econ.CEcon_GetInventoryItemsWithDescriptions_Request_FilterOptions
import steam.econ.Econ
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EconRepository @Inject constructor(
    steamRpcClient: SteamRpcClient,
    private val localeService: LocaleService
) {
    private val stub = steamRpcClient.create<Econ>()

    suspend fun getInventoryItemsWithDescriptions(
        steamid: Long,
        appid: Int,
        contextid: Long,
        getDescriptions: Boolean,
        assetids: List<Long>
    ) = stub.GetInventoryItemsWithDescriptions(
        CEcon_GetInventoryItemsWithDescriptions_Request(
            steamid = steamid,
            appid = appid,
            contextid = contextid,
            get_descriptions = getDescriptions,
            filters = CEcon_GetInventoryItemsWithDescriptions_Request_FilterOptions(assetids = assetids),
            language = localeService.language
        )
    )
}
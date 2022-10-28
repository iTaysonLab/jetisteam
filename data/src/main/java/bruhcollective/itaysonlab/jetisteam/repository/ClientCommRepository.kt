package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import bruhcollective.itaysonlab.jetisteam.util.LanguageUtil
import steam.clientcomm.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClientCommRepository @Inject constructor(
    steamRpcClient: SteamRpcClient
) {
    private val stub = steamRpcClient.create<ClientComm>()

    suspend fun getActiveClients() = stub.GetAllClientLogonInfo(
        CClientComm_GetAllClientLogonInfo_Request()
    ).sessions

    suspend fun getInstalledApps(clientId: Long) = stub.GetClientAppList(
        CClientComm_GetClientAppList_Request(
            client_instanceid = clientId,
            language = LanguageUtil.currentLanguage,
            include_client_info = false,
            fields = "",
            filters = ""
        )
    )

    suspend fun addToInstallQueue(clientId: Long, appId: Int) = stub.InstallClientApp(
        CClientComm_InstallClientApp_Request(
            appid = appId,
            client_instanceid = clientId
        )
    )

    suspend fun uninstall(clientId: Long, appId: Int) = stub.UninstallClientApp(
        CClientComm_UninstallClientApp_Request(
            appid = appId,
            client_instanceid = clientId
        )
    )

    suspend fun getClientInfo(clientId: Long) = stub.GetClientInfo(
        CClientComm_GetClientInfo_Request(clientId)
    )

    suspend fun toggleActiveDownload(clientId: Long, active: Boolean) = stub.EnableOrDisableDownloads(
        CClientComm_EnableOrDisableDownloads_Request(clientId, enable = active)
    )
}
package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import steam.steamnotification.CSteamNotification_GetSteamNotifications_Request
import steam.steamnotification.SteamNotification
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationsRepository @Inject constructor(
    steamRpcClient: SteamRpcClient,
) {
    private val stub = steamRpcClient.create<SteamNotification>()

    suspend fun getNotifications() = stub.GetSteamNotifications(CSteamNotification_GetSteamNotifications_Request())
}
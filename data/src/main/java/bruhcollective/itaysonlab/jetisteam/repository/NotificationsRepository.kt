package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcChannel
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcController
import bruhcollective.itaysonlab.jetisteam.util.LanguageUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import steam.steamnotification.CSteamNotification_GetSteamNotifications_Request
import steam.steamnotification.SteamNotification
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationsRepository @Inject constructor(
    steamRpcChannel: SteamRpcChannel,
) {
    private val stub = SteamNotification.newBlockingStub(steamRpcChannel)

    suspend fun getNotifications() = withContext(Dispatchers.IO) {
        stub.getSteamNotifications(
            SteamRpcController(), CSteamNotification_GetSteamNotifications_Request.getDefaultInstance()
        )
    }
}
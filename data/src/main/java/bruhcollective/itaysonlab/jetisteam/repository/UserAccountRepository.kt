package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcChannel
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import steam.useraccount.CUserAccount_GetUserCountry_Request
import steam.useraccount.UserAccount
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAccountRepository @Inject constructor(
    steamRpcChannel: SteamRpcChannel
) {
    private val stub = UserAccount.newBlockingStub(steamRpcChannel)

    suspend fun getUserCountry(steamid: Long) = withContext(Dispatchers.IO) {
        stub.getUserCountry(
            SteamRpcController(post = true), CUserAccount_GetUserCountry_Request.newBuilder()
                .setSteamid(steamid)
                .build()
        ).country
    }
}
package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import steam.useraccount.CUserAccount_GetUserCountry_Request
import steam.useraccount.UserAccount
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserAccountRepository @Inject constructor(
    steamRpcClient: SteamRpcClient,
) {
    private val stub = steamRpcClient.create<UserAccount>()

    suspend fun getUserCountry(steamid: Long) = stub.GetUserCountry(CUserAccount_GetUserCountry_Request(steamid)).country.orEmpty()
}
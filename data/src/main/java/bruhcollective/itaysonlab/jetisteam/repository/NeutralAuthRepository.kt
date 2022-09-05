package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import kotlinx.coroutines.runBlocking
import steam.auth.Authentication
import steam.auth.CAuthentication_AccessToken_GenerateForApp_Request
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class NeutralAuthRepository @Inject constructor(
    @Named("neutralRpcClient") steamRpcClient: SteamRpcClient,
    private val steamSessionController: SteamSessionController
) {
    private val stub = steamRpcClient.create<Authentication>()

    fun refreshSession() {
        val authSession = steamSessionController.authSession!!

        runBlocking {
            stub.GenerateAccessTokenForApp(
                CAuthentication_AccessToken_GenerateForApp_Request(
                    refresh_token = authSession.refresh_token,
                    steamid = authSession.steam_id,
                )
            )
        }.also {
            steamSessionController.writeSession(authSession.copy(
                access_token = it.access_token.orEmpty()
            ))
        }
    }
}
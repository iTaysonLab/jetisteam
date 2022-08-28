package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcChannel
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcController
import steam.auth.Authentication
import steam.auth.CAuthentication_AccessToken_GenerateForApp_Request
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class NeutralAuthRepository @Inject constructor(
    @Named("neutralRpcChannel") steamRpcChannel: SteamRpcChannel,
    private val steamSessionController: SteamSessionController
) {
    private val stub = Authentication.newBlockingStub(steamRpcChannel)

    fun refreshSession() {
        val authSession = steamSessionController.authSession!!

        stub.generateAccessTokenForApp(
            SteamRpcController(post = true), CAuthentication_AccessToken_GenerateForApp_Request.newBuilder()
                .setRefreshToken(authSession.refreshToken)
                .setSteamid(authSession.steamId)
                .build()
        ).also {
            steamSessionController.writeSession(authSession.toBuilder().setAccessToken(it.accessToken).build())
        }
    }
}
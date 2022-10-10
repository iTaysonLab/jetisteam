package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import steam.twofactor.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TwoFactorRepository @Inject constructor(
    steamRpcClient: SteamRpcClient
) {
    private val stub = steamRpcClient.create<TwoFactor>()

    suspend fun addAuthenticator(steamID: SteamID, uuid: String) = stub.AddAuthenticator(
        CTwoFactor_AddAuthenticator_Request(
            steamid = steamID.steamId,
            authenticator_type = 1,
            sms_phone_id = "1",
            version = 2,
            device_identifier = uuid
        )
    ).let { response ->
        if (response.status == 29) {
            AddAuthenticatorResponse.AlreadyExists
        } else {
            AddAuthenticatorResponse.WaitingForPhoneConfirmation(response)
        }
    }

    suspend fun moveStart() = stub.RemoveAuthenticatorViaChallengeStart(
        CTwoFactor_RemoveAuthenticatorViaChallengeStart_Request()
    )

    suspend fun moveFinish(code: String) = stub.RemoveAuthenticatorViaChallengeContinue(
        CTwoFactor_RemoveAuthenticatorViaChallengeContinue_Request(
            sms_code = code,
            generate_new_token = true,
            version = 2
        )
    )

    sealed class AddAuthenticatorResponse {
        class WaitingForPhoneConfirmation(val wrapped: CTwoFactor_AddAuthenticator_Response): AddAuthenticatorResponse()
        object AlreadyExists: AddAuthenticatorResponse()
    }
}
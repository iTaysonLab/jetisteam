package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import steam.twofactor.CTwoFactor_AddAuthenticator_Request
import steam.twofactor.CTwoFactor_AddAuthenticator_Response
import steam.twofactor.TwoFactor
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

    sealed class AddAuthenticatorResponse {
        class WaitingForPhoneConfirmation(val wrapped: CTwoFactor_AddAuthenticator_Response): AddAuthenticatorResponse()
        object AlreadyExists: AddAuthenticatorResponse()
    }
}
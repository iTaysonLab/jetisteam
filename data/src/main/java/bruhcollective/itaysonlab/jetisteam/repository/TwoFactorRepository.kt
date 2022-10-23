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

    suspend fun remove(
        rvCode: String, reason: Int, scheme: Int, removeCookies: Boolean
    ) = stub.RemoveAuthenticator(CTwoFactor_RemoveAuthenticator_Request(
        revocation_code = rvCode,
        revocation_reason = reason,
        steamguard_scheme = scheme,
        remove_all_steamguard_cookies = removeCookies
    ))

    suspend fun finalizeWithSms(
        steamID: SteamID, code: String
    ) = stub.FinalizeAddAuthenticator(
        CTwoFactor_FinalizeAddAuthenticator_Request(
            steamid = steamID.steamId,
            activation_code = code,
            validate_sms_code = true
        )
    )

    suspend fun finalizeWithCode(
        steamID: SteamID, code: String, codeGenTime: Long
    ) = stub.FinalizeAddAuthenticator(
        CTwoFactor_FinalizeAddAuthenticator_Request(
            steamid = steamID.steamId,
            authenticator_code = code,
            authenticator_time = codeGenTime
        )
    )

    sealed class AddAuthenticatorResponse {
        class WaitingForPhoneConfirmation(val wrapped: CTwoFactor_AddAuthenticator_Response): AddAuthenticatorResponse()
        object AlreadyExists: AddAuthenticatorResponse()
    }
}
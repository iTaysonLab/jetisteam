package bruhcollective.itaysonlab.jetisteam.repository

import android.util.Base64
import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.controllers.UuidController
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.proto.SessionData
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import okio.ByteString
import steam.auth.*
import java.math.BigInteger
import java.security.KeyFactory
import java.security.PublicKey
import java.security.spec.RSAPublicKeySpec
import javax.crypto.Cipher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(
    steamRpcClient: SteamRpcClient,
    private val steamSessionController: SteamSessionController,
    private val uuidController: UuidController
) {
    private val stub = steamRpcClient.create<Authentication>()
    private var currentCredsSession: CAuthentication_BeginAuthSessionViaCredentials_Response? = null

    suspend fun getRsaKey(username: String) = stub.GetPasswordRSAPublicKey(
        CAuthentication_GetPasswordRSAPublicKey_Request(account_name = username)
    ).let { response ->
        generateRsaKey(response.publickey_mod.orEmpty(), response.publickey_exp.orEmpty()) to (response.timestamp ?: 0)
    }

    suspend fun beginAuthSessionViaCredentials(
        username: String,
        encryptedTimestamp: Long,
        encryptedPassword: ByteArray
    ) = stub.BeginAuthSessionViaCredentials(
        CAuthentication_BeginAuthSessionViaCredentials_Request(
            account_name = username,
            encryption_timestamp = encryptedTimestamp,
            encrypted_password = encryptedPassword.decodeToString(),
            remember_login = true,
            platform_type = EAuthTokenPlatformType.k_EAuthTokenPlatformType_MobileApp,
            persistence = ESessionPersistence.k_ESessionPersistence_Persistent,
            website_id = "Mobile",
            device_details = CAuthentication_DeviceDetails(
                device_friendly_name = "${uuidController.deviceName} (Jetisteam)",
                platform_type = EAuthTokenPlatformType.k_EAuthTokenPlatformType_MobileApp,
                os_type = uuidController.osType,
                gaming_device_type = EGamingDeviceType.k_EGamingDeviceType_Phone
            )
        )
    ).also { currentCredsSession = it }.let {
        BeginAuthModel(it.allowed_confirmations.isNotEmpty() to (it.allowed_confirmations.firstOrNull { c -> c.confirmation_type == EAuthSessionGuardType.k_EAuthSessionGuardType_DeviceConfirmation } != null))
    }

    suspend fun enterDeviceCode(
        code: String
    ): Boolean {
        currentCredsSession ?: return false

        stub.UpdateAuthSessionWithSteamGuardCode(
            CAuthentication_UpdateAuthSessionWithSteamGuardCode_Request(
                client_id = currentCredsSession!!.client_id,
                steamid = currentCredsSession!!.steamid,
                code_type = currentCredsSession!!.allowed_confirmations.first().confirmation_type,
                code = code
            )
        )

        return pollStatus()
    }

    suspend fun pollStatus(): Boolean {
        currentCredsSession ?: return false

        return stub.PollAuthSessionStatus(
            CAuthentication_PollAuthSessionStatus_Request(
                client_id = currentCredsSession!!.client_id,
                request_id = currentCredsSession!!.request_id,
            )
        ).let { response ->
            return@let if (response.access_token != null) {
                steamSessionController.writeSession(
                    SessionData(
                        steam_id = currentCredsSession!!.steamid ?: 0,
                        username = response.account_name.orEmpty(),
                        access_token = response.access_token,
                        refresh_token = response.refresh_token.orEmpty(),
                    )
                )
                true
            } else {
                false
            }
        }
    }

    suspend fun enumerateTokens() = stub.EnumerateTokens(CAuthentication_RefreshToken_Enumerate_Request())
    suspend fun getAuthSession(clientId: Long) = stub.GetAuthSessionInfo(CAuthentication_GetAuthSessionInfo_Request(client_id = clientId))
    suspend fun getQueueOfSessions() = stub.GetAuthSessionsForAccount(CAuthentication_GetAuthSessionsForAccount_Request())
    suspend fun updateSessionWithMobileAuth(
        version: Int,
        clientId: Long,
        steamId: Long,
        signature: ByteString,
        confirm: Boolean,
        persistence: ESessionPersistence
    ) = stub.UpdateAuthSessionWithMobileConfirmation(CAuthentication_UpdateAuthSessionWithMobileConfirmation_Request(
        version = version,
        client_id = clientId,
        steamid = steamId,
        signature = signature,
        confirm = confirm,
        persistence = persistence
    ))

    suspend fun revokeSession(
        steamId: SteamID, tokenId: Long, signature: ByteString
    ) = stub.RevokeRefreshToken(CAuthentication_RefreshToken_Revoke_Request(
        steamid = steamId.steamId,
        token_id = tokenId,
        signature = signature,
        revoke_action = EAuthTokenRevokeAction.k_EAuthTokenRevokePermanent
    ))

    private fun generateRsaKey(
        mod: String, exp: String
    ) = KeyFactory.getInstance("RSA")
        .generatePublic(RSAPublicKeySpec(BigInteger(mod, 16), BigInteger(exp, 16)))

    fun encryptPassword(
        pwd: String, key: PublicKey
    ): ByteArray = Base64.encode(
        Cipher.getInstance("RSA/None/PKCS1Padding").also { it.init(Cipher.ENCRYPT_MODE, key) }.doFinal(pwd.encodeToByteArray()),
        Base64.NO_PADDING or Base64.NO_WRAP
    )

    @JvmInline
    value class BeginAuthModel(private val packed: Pair<Boolean, Boolean>) {
        val doesInfoMatch: Boolean get() = packed.first
        val canUseRemoteConfirmation: Boolean get() = packed.second
    }
}
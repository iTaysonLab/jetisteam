package bruhcollective.itaysonlab.jetisteam.repository

import android.util.Base64
import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.proto.SessionData
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcChannel
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcController
import com.google.protobuf.ByteString
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    steamRpcChannel: SteamRpcChannel,
    private val steamSessionController: SteamSessionController
) {
    private val stub = Authentication.newBlockingStub(steamRpcChannel)
    private var currentCredsSession: CAuthentication_BeginAuthSessionViaCredentials_Response? = null

    suspend fun getRsaKey(username: String) = withContext(Dispatchers.IO) {
        stub.getPasswordRSAPublicKey(
            SteamRpcController(),
            CAuthentication_GetPasswordRSAPublicKey_Request.newBuilder().setAccountName(username)
                .build()
        ).let { response ->
            generateRsaKey(response.publickeyMod, response.publickeyExp) to response.timestamp
        }
    }

    suspend fun beginAuthSessionViaCredentials(
        username: String,
        encryptedTimestamp: Long,
        encryptedPassword: ByteArray
    ) = withContext(Dispatchers.IO) {
        stub.beginAuthSessionViaCredentials(
            SteamRpcController(post = true), CAuthentication_BeginAuthSessionViaCredentials_Request
                .newBuilder()
                .setAccountName(username)
                .setEncryptionTimestamp(encryptedTimestamp)
                .setEncryptedPasswordBytes(ByteString.copyFrom(encryptedPassword))
                .setRememberLogin(true)
                .setPlatformType(EAuthTokenPlatformType.k_EAuthTokenPlatformType_MobileApp)
                .setPersistence(ESessionPersistence.k_ESessionPersistence_Persistent)
                .setWebsiteId("Mobile")
                .build()
        ).also { currentCredsSession = it }.allowedConfirmationsList.isNotEmpty()
    }

    suspend fun enterDeviceCode(
        code: String
    ): Boolean = withContext(Dispatchers.IO) {
        stub.updateAuthSessionWithSteamGuardCode(
            SteamRpcController(post = true), CAuthentication_UpdateAuthSessionWithSteamGuardCode_Request
                .newBuilder()
                .setClientId(currentCredsSession!!.clientId)
                .setSteamid(currentCredsSession!!.steamid)
                .setCodeType(currentCredsSession!!.allowedConfirmationsList[0].confirmationType)
                .setCode(code)
                .build()
        )

        return@withContext stub.pollAuthSessionStatus(
            SteamRpcController(post = true), CAuthentication_PollAuthSessionStatus_Request
                .newBuilder()
                .setClientId(currentCredsSession!!.clientId)
                .setRequestId(currentCredsSession!!.requestId)
                .build()
        ).let { response ->
            return@let if (response.hasAccessToken()) {
                steamSessionController.writeSession(
                    SessionData
                        .newBuilder()
                        .setSteamId(currentCredsSession!!.steamid)
                        .setUsername(response.accountName)
                        .setAccessToken(response.accessToken)
                        .setRefreshToken(response.refreshToken)
                        .build()
                )
                true
            } else {
                false
            }
        }
    }

    suspend fun refreshSession() = withContext(Dispatchers.IO) {

    }

    private fun generateRsaKey(
        mod: String, exp: String
    ) = KeyFactory.getInstance("RSA", "BC")
        .generatePublic(RSAPublicKeySpec(BigInteger(mod, 16), BigInteger(exp, 16)))

    fun encryptPassword(
        pwd: String, key: PublicKey
    ): ByteArray = Base64.encode(
        Cipher.getInstance("RSA/None/PKCS1Padding", "BC").also { it.init(Cipher.ENCRYPT_MODE, key) }.doFinal(pwd.encodeToByteArray()),
        Base64.NO_PADDING or Base64.NO_WRAP
    )
}
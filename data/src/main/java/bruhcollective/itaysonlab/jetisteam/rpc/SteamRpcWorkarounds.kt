package bruhcollective.itaysonlab.jetisteam.rpc

import android.util.Base64
import com.squareup.wire.Message
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.MultipartBody
import okhttp3.Request

object SteamRpcWorkarounds {
    private const val FIELD_NAME = "input_protobuf_encoded"
    private const val API_URL = "https://api.steampowered.com"

    private val forcePostFor = arrayOf(
        // Authentication
        "/Authentication/BeginAuthSessionViaCredentials",
        "/Authentication/UpdateAuthSessionWithSteamGuardCode",
        "/Authentication/UpdateAuthSessionWithSteamGuardCode",
        "/Authentication/PollAuthSessionStatus",
        "/Authentication/GenerateAccessTokenForApp",
        "/Authentication/EnumerateTokens",
        // Player
        "/Player/GetAchievementsProgress",
        // MobileApp
        "/MobileApp/GetMobileSummary",
        // UserAccount
        "/UserAccount/GetUserCountry",
        // TwoFactor
        "/TwoFactor/AddAuthenticator",
        "/TwoFactor/FinalizeAddAuthenticator",
        "/TwoFactor/RemoveAuthenticatorViaChallengeStart",
        "/TwoFactor/RemoveAuthenticatorViaChallengeContinue",
    )

    /**
     * Steam enforces GET/POST requests for some of the RPC methods.
     * The best method without manual writing or using Google's Protobuf runtime is using predefined "allowlist"
     */
    fun shouldUsePostFor(path: String) = path in forcePostFor

    /**
     * Steam uses RPC-over-HTTP based on their legacy Web API
     * While the RPC service names are proper, we need to add version number + "Service" suffix (nameSuffix in build.gradle won't work)
     */
    fun formatUrl(packed: SteamRpcProcessor.ServiceAndMethod) =
        "${API_URL}/I${packed.service}Service/${packed.method}/v1"

    /**
     * Steam does not use the typical gRPC message transfer specification, so we should create a different request body for each method
     */
    fun createRequest(url: String, message: Message<*, *>, post: Boolean) =
        Request.Builder().apply {
            val msgBytes = Base64.encodeToString(message.encode(), Base64.NO_PADDING or Base64.NO_WRAP)

            url(url.toHttpUrl().modifyIf(!post) {
                newBuilder().addQueryParameter(FIELD_NAME, msgBytes).build()
            })

            if (post) {
                post(MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(FIELD_NAME, msgBytes).build())
            } else {
                get()
            }
        }.build()
}
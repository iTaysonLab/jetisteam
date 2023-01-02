package bruhcollective.itaysonlab.jetisteam.rpc

import android.util.Base64
import bruhcollective.itaysonlab.jetisteam.controllers.SteamWebApiTokenController
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
        "/Authentication/GetAuthSessionInfo",
        "/Authentication/UpdateAuthSessionWithMobileConfirmation",
        "/Authentication/RevokeRefreshToken",
        // Player
        "/Player/GetAchievementsProgress",
        "/Player/SetAnimatedAvatar",
        "/Player/SetAvatarFrame",
        "/Player/SetCommunityPreferences",
        "/Player/SetEquippedProfileItemFlags",
        "/Player/SetMiniProfileBackground",
        "/Player/SetPerFriendPreferences",
        "/Player/SetProfileBackground",
        "/Player/SetProfilePreferences",
        "/Player/SetProfileTheme",
        // MobileApp
        "/MobileApp/GetMobileSummary",
        // UserAccount
        "/UserAccount/GetUserCountry",
        // TwoFactor
        "/TwoFactor/AddAuthenticator",
        "/TwoFactor/FinalizeAddAuthenticator",
        "/TwoFactor/RemoveAuthenticatorViaChallengeStart",
        "/TwoFactor/RemoveAuthenticatorViaChallengeContinue",
        "/TwoFactor/RemoveAuthenticator",
        // Quest
        "/Quest/ActivateProfileModifierItem",
        // ClientComm
        "/ClientComm/EnableOrDisableDownloads",
        "/ClientComm/InstallClientApp",
        "/ClientComm/UninstallClientApp",
        "/ClientComm/SetClientAppUpdateState",
    )

    private val webApiTokenMap = mapOf(
        "/SaleFeature/GetUserYearInReview" to SteamWebApiTokenController.TokenRealm.SteamReplay,
        "/SaleFeature/GetUserYearAchievements" to SteamWebApiTokenController.TokenRealm.SteamReplay,
        "/SaleFeature/GetFriendsSharedYearInReview" to SteamWebApiTokenController.TokenRealm.SteamReplay,
    )

    /**
     * Steam enforces GET/POST requests for some of the RPC methods.
     * The best method without manual writing or using Google's Protobuf runtime is using predefined "allowlist"
     */
    fun shouldUsePostFor(path: String) = path in forcePostFor

    /**
     * Steam enforces some of the requests to use a special WebApi token, not a generic OAuth received by signing in
     * They only (?) return that in web pages, embedded, so we use a regex parsing of a webpage to obtain such token
     */
    fun shouldUseWebApiController(path: String) = webApiTokenMap[path]

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
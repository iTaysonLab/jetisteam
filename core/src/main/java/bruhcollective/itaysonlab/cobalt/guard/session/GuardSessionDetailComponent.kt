package bruhcollective.itaysonlab.cobalt.guard.session

import bruhcollective.itaysonlab.ksteam.models.enums.EGamingDeviceType
import bruhcollective.itaysonlab.ksteam.models.enums.EOSType
import com.arkivanov.decompose.value.Value
import steam.enums.EAuthSessionGuardType
import steam.enums.EAuthTokenPlatformType

interface GuardSessionDetailComponent {
    val isRevocationAlertOpened: Value<Boolean>
    val isRevocationInProgress: Value<Boolean>

    val deviceName: String
    val platformType: EAuthTokenPlatformType
    val deviceType: EGamingDeviceType
    val guardType: EAuthSessionGuardType
    val osType: EOSType

    val firstSeen: Int?
    val lastSeen: Int?
    val lastSeenLocationString: String?
    val lastSeenIp: String?

    val isCurrentSession: Boolean

    fun openRevokeScreen()
    fun dismissRevokeScreen()
    fun revokeSession()

    fun openSupportPage()

    fun onBackClicked()
}
package bruhcollective.itaysonlab.cobalt.guard.session

import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.ksteam.models.enums.EGamingDeviceType
import bruhcollective.itaysonlab.ksteam.models.enums.EOSType
import com.arkivanov.decompose.ComponentContext
import org.koin.core.component.KoinComponent
import steam.enums.EAuthSessionGuardType
import steam.enums.EAuthTokenPlatformType

internal class DefaultGuardSessionDetailComponent (
    private val session: ActiveSession,
    private val onDismiss: () -> Unit,
    componentContext: ComponentContext
): GuardSessionDetailComponent, KoinComponent, ComponentContext by componentContext {
    override val deviceName: String get() = session.deviceName
    override val platformType: EAuthTokenPlatformType get() = session.platformType
    override val deviceType: EGamingDeviceType get() = session.gamingDeviceType
    override val guardType: EAuthSessionGuardType get() = session.confirmedWith
    override val osType: EOSType get() = session.osType
    override val firstSeen: Int? get() = session.firstSeen?.time
    override val lastSeen: Int get() = session.lastSeen?.time ?: session.timeUpdated
    override val lastSeenLocationString: String? get() = session.lastSeen?.let { ls -> "${ls.city}, ${ls.state}, ${ls.country}" }
    override val lastSeenIp: String? get() = session.lastSeen?.ipString

    override fun revokeSession() {
        TODO("Not yet implemented")
    }

    override fun openSupportPage() {
        TODO("Not yet implemented")
    }

    override fun onBackClicked() = onDismiss()
}
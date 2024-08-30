package bruhcollective.itaysonlab.cobalt.guard.session

import bruhcollective.itaysonlab.cobalt.core.platform.PlatformBrowser
import bruhcollective.itaysonlab.ksteam.ExtendedSteamClient
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.ksteam.models.enums.EGamingDeviceType
import bruhcollective.itaysonlab.ksteam.models.enums.EOSType
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import steam.enums.EAuthSessionGuardType
import steam.enums.EAuthTokenPlatformType

internal class DefaultGuardSessionDetailComponent (
    private val session: ActiveSession,
    private val onDismiss: () -> Unit,
    private val onSessionRemoved: () -> Unit,
    componentContext: ComponentContext
): GuardSessionDetailComponent, KoinComponent, ComponentContext by componentContext {
    private val scope = coroutineScope()

    override val isRevocationAlertOpened = MutableValue(false)
    override val isRevocationInProgress = MutableValue(false)

    override val deviceName: String get() = session.deviceName
    override val platformType: EAuthTokenPlatformType get() = session.platformType
    override val deviceType: EGamingDeviceType get() = session.gamingDeviceType
    override val guardType: EAuthSessionGuardType get() = session.confirmedWith
    override val osType: EOSType get() = session.osType
    override val firstSeen: Int? get() = session.firstSeen?.time
    override val lastSeen: Int get() = session.lastSeen?.time ?: session.timeUpdated
    override val lastSeenLocationString: String? get() = session.lastSeen?.let { ls -> "${ls.city}, ${ls.state}, ${ls.country}" }
    override val lastSeenIp: String? get() = session.lastSeen?.ip
    override val isCurrentSession: Boolean = session.isCurrentSession

    override fun openRevokeScreen() {
        isRevocationAlertOpened.value = true
    }

    override fun dismissRevokeScreen() {
        isRevocationAlertOpened.value = false
    }

    override fun revokeSession() {
        isRevocationInProgress.value = true

        scope.launch {
            get<ExtendedSteamClient>().guardManagement.revokeSession(session.id)
            onSessionRemoved()
        }
    }

    override fun openSupportPage() {
        get<PlatformBrowser>().openLink("https://help.steampowered.com/")
    }

    override fun onBackClicked() = onDismiss()
}
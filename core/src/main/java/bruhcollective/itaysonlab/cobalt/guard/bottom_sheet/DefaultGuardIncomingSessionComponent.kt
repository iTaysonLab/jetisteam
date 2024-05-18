package bruhcollective.itaysonlab.cobalt.guard.bottom_sheet

import bruhcollective.itaysonlab.ksteam.ExtendedSteamClient
import bruhcollective.itaysonlab.ksteam.models.SteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnStart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class DefaultGuardIncomingSessionComponent (
    private val onDismiss: () -> Unit,
    componentContext: ComponentContext,
    steamId: SteamId,
    sessionId: Long,
): GuardIncomingSessionComponent, ComponentContext by componentContext, KoinComponent, CoroutineScope by componentContext.coroutineScope() {
    private val client = get<ExtendedSteamClient>()
    private val guard = client.guard.instanceFor(steamId)

    override val username: String = guard?.username.orEmpty()
    override val state = MutableValue<GuardIncomingSessionComponent.State>(GuardIncomingSessionComponent.State.Loading)
    override val shouldRememberPassword = MutableValue(false)
    override val isConfirmationInProgress = MutableValue(false)
    override val isCancellationInProgress = MutableValue(false)

    override fun setShouldRememberPassword(value: Boolean) {
        shouldRememberPassword.value = value
    }

    init {
        lifecycle.doOnStart(isOneTime = true) {
            launch {
                client.guardManagement.getIncomingSessionInfo(sessionId)?.let { info ->
                    shouldRememberPassword.value = info.requestedPersistedSession
                    state.value = GuardIncomingSessionComponent.State.Ready(info)
                }
            }
        }
    }

    override fun confirmSession() {
        launch {
            isConfirmationInProgress.value = true

            (state.value as? GuardIncomingSessionComponent.State.Ready)?.let { loadedSession ->
                client.guardManagement.approveIncomingSession(
                    session = loadedSession.session,
                    persist = shouldRememberPassword.value
                )
            }

            onDismiss()
        }
    }

    override fun cancelSession() {
        launch {
            isCancellationInProgress.value = true

            (state.value as? GuardIncomingSessionComponent.State.Ready)?.let { loadedSession ->
                client.guardManagement.rejectIncomingSession(session = loadedSession.session)
            }

            onDismiss()
        }
    }
}
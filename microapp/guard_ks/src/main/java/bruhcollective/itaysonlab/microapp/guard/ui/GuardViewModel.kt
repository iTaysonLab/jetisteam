package bruhcollective.itaysonlab.microapp.guard.ui

import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.ksteam.guard.GuardInstance
import bruhcollective.itaysonlab.ksteam.handlers.currentSessionSteamId
import bruhcollective.itaysonlab.ksteam.handlers.guard
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class GuardViewModel @Inject constructor(
    private val hostSteamClient: HostSteamClient
): ViewModel() {
    val state by lazy {
        when (val instance = hostSteamClient.client.guard.instanceForCurrentUser()) {
            null -> GuardState.Setup
            else -> GuardState.Available(instance = instance)
        }
    }

    val steamId get() = hostSteamClient.client.currentSessionSteamId.longId

    private var isPollingActive = state is GuardState.Available

    /*var confirmationFlow = flow {
        coroutineScope {
            while (isPollingActive) {
                emit(getQueueOfSessions())
                delay(5000L)
            }
        }
    }*/

    sealed class GuardState {
        class Available(val instance: GuardInstance): GuardState()
        object Setup: GuardState()
    }
}
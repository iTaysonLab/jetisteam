package bruhcollective.itaysonlab.microapp.guard.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.ksteam.guard.GuardInstance
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.ksteam.guard.models.ConfirmationListState
import bruhcollective.itaysonlab.ksteam.guard.models.MobileConfirmationItem
import bruhcollective.itaysonlab.ksteam.handlers.account
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.ksteam.handlers.guardConfirmation
import bruhcollective.itaysonlab.ksteam.handlers.guardManagement
import bruhcollective.itaysonlab.ksteam.models.SteamId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.ByteString.Companion.encodeUtf8
import javax.inject.Inject

@HiltViewModel
internal class GuardViewModel @Inject constructor(
    private val hostSteamClient: HostSteamClient
): ViewModel() {
    private val json = Json {
        ignoreUnknownKeys = true
    }

    var confirmations by mutableStateOf<ConfirmationListState>(ConfirmationListState.Loading)
    var sessions by mutableStateOf<List<ActiveSession>?>(null)

    val state by lazy {
        when (val instance = hostSteamClient.client.guard.instanceFor(SteamId(hostSteamClient.client.account.getDefaultAccount()!!.steamId))) {
            null -> GuardState.Setup
            else -> GuardState.Available(instance = instance)
        }
    }

    val steamId get() = hostSteamClient.client.currentSessionSteamId.longId

    var awaitingSessionPoll = hostSteamClient.client.guardManagement.createSessionWatcher()

    init {
        viewModelScope.launch {
            (state as? GuardState.Available)?.let {
                hostSteamClient.client.account.awaitSignIn()
                confirmations = hostSteamClient.client.guardConfirmation.getConfirmations(it.instance)
                sessions = hostSteamClient.client.guardManagement.getActiveSessions()
            }
        }
    }

    fun wrapConfirmation(confirmation: MobileConfirmationItem): String {
        return json.encodeToString(confirmation).encodeUtf8().base64Url()
    }

    sealed class GuardState {
        class Available(val instance: GuardInstance): GuardState()
        object Setup: GuardState()
    }
}
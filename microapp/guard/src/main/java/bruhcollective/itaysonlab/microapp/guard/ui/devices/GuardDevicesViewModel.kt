package bruhcollective.itaysonlab.microapp.guard.ui.devices

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.usecases.twofactor.GetAuthorizedDevices
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import steam.auth.CAuthentication_RefreshToken_Enumerate_Response
import javax.inject.Inject

@HiltViewModel
internal class GuardDevicesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAuthorizedDevices: GetAuthorizedDevices
) : ViewModel() {
    var state by mutableStateOf<State>(State.Loading)
        private set

    val steamId = savedStateHandle.getSteamId()

    init {
        viewModelScope.launch {
            loadSessions()
        }
    }

    private suspend fun loadSessions() {
        state = State.Ready(getAuthorizedDevices())
    }

    sealed class State {
        class Ready(
            val data: List<CAuthentication_RefreshToken_Enumerate_Response.RefreshTokenDescription>
        ) : State()

        object Loading : State()
    }
}
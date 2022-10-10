package bruhcollective.itaysonlab.microapp.guard.ui.devices

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.usecases.twofactor.GetAuthorizedDevices
import bruhcollective.itaysonlab.microapp.guard.GuardMicroappImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class GuardDevicesViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getAuthorizedDevices: GetAuthorizedDevices
): ViewModel() {
    var state by mutableStateOf<State>(State.Loading)
        private set

    val steamId = SteamID(savedStateHandle.get<String>(GuardMicroappImpl.InternalRoutes.ARG_STEAM_ID)!!.toLong())

    init {
        viewModelScope.launch {
            loadSessions()
        }
    }

    private suspend fun loadSessions() {

    }

    sealed class State {
        class Ready(

        ): State()

        object Loading: State()
    }
}
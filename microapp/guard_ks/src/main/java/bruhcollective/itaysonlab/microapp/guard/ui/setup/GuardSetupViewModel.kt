package bruhcollective.itaysonlab.microapp.guard.ui.setup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GuardSetupViewModel @Inject constructor(
    private val hostSteamClient: HostSteamClient,
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val steamId = savedStateHandle.getSteamId()
    val tfaAddState get() = hostSteamClient.client.guard.guardConfigurationFlow

    init {
        viewModelScope.launch {
            hostSteamClient.client.guard.initializeSgCreation()
        }
    }

    fun confirmMove() {
        viewModelScope.launch {
            hostSteamClient.client.guard.confirmMove()
        }
    }

    fun submitVerificationCode(code: String) {
        viewModelScope.launch {
            hostSteamClient.client.guard.confirmSgConfiguration(code)
        }
    }
}
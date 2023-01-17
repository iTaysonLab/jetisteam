package bruhcollective.itaysonlab.microapp.auth.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.ksteam.handlers.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TfaScreenViewModel @Inject constructor(
    hostSteamClient: HostSteamClient,
): ViewModel(), CoroutineScope by MainScope() {
    private val accountHandler = hostSteamClient.client.getHandler<Account>()
    val authFlow get() = accountHandler.clientAuthState

    var isAuthInProgress by mutableStateOf(false)
    var isCodeError by mutableStateOf(false)

    fun enterCode(code: String, onSuccess: () -> Unit) {
        isCodeError = false

        if (code.isEmpty()) {
            isCodeError = true
            return
        }

        viewModelScope.launch {
            isAuthInProgress = true

            if (accountHandler.updateCurrentSessionWithCode(code)) {
                onSuccess()
            } else {
                isCodeError = true
            }

            isAuthInProgress = false
        }
    }

    override fun onCleared() {
        accountHandler.cancelPolling()
    }
}
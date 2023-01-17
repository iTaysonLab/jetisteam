package bruhcollective.itaysonlab.microapp.auth.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.ksteam.handlers.Account
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AuthScreenViewModel @Inject constructor(hostSteamClient: HostSteamClient) : ViewModel() {
    private val accountHandler = hostSteamClient.client.getHandler<Account>()

    var isAuthInProgress by mutableStateOf(false)
    var isEmailError by mutableStateOf(false)
    var isPasswordError by mutableStateOf(false)

    fun auth(username: String, password: String, onSuccess: () -> Unit) {
        isEmailError = false
        isPasswordError = false

        if (username.isEmpty()) {
            isEmailError = true
        }

        if (password.isEmpty()) {
            isPasswordError = true
        }

        if (isEmailError || isPasswordError) return

        viewModelScope.launch {
            isAuthInProgress = true

            val data = accountHandler.signIn(username, password)

            if (data == Account.AuthorizationResult.Success) {
                onSuccess()
            } else {
                isEmailError = true
                isPasswordError = true
            }

            isAuthInProgress = false
        }
    }
}
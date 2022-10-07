package bruhcollective.itaysonlab.microapp.auth.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.usecases.auth.BeginSignIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class AuthScreenViewModel @Inject constructor(
    private val beginSignIn: BeginSignIn
) : ViewModel() {
    var isAuthInProgress by mutableStateOf(false)
    var isEmailError by mutableStateOf(false)
    var isPasswordError by mutableStateOf(false)

    fun auth(username: String, password: String, onSuccess: (Boolean) -> Unit) {
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

            val data = beginSignIn(username, password)

            if (data.doesInfoMatch) {
                onSuccess(data.canUseRemoteConfirmation)
            } else {
                isEmailError = true
                isPasswordError = true
            }

            isAuthInProgress = false
        }
    }
}
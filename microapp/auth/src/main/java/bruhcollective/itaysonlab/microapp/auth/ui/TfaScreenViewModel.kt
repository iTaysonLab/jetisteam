package bruhcollective.itaysonlab.microapp.auth.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.usecases.EnterCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TfaScreenViewModel @Inject constructor(
    private val enterCode: EnterCode
): ViewModel() {
    var isAuthInProgress by mutableStateOf(false)
    var isCodeError by mutableStateOf(false)

    fun enterCode(code: String, onSuccess: () -> Unit) {
        isCodeError = false

        if (code.isEmpty()) {
            isCodeError = true
        }

        if (isCodeError) return

        viewModelScope.launch {
            isAuthInProgress = true

            if (enterCode(code)) {
                onSuccess()
            } else {
                isCodeError = true
            }

            isAuthInProgress = false
        }
    }
}
package bruhcollective.itaysonlab.microapp.auth.ui

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.usecases.auth.EnterCode
import bruhcollective.itaysonlab.jetisteam.usecases.auth.PollAuthSession
import bruhcollective.itaysonlab.microapp.auth.AuthMicroappImpl
import bruhcollective.itaysonlab.microapp.profile.ProfileMicroappImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class TfaScreenViewModel @Inject constructor(
    private val enterCode: EnterCode,
    private val pollAuthSession: PollAuthSession,
    savedState: SavedStateHandle,
): ViewModel(), CoroutineScope by MainScope() {
    var isPollingActive = savedState.get<String>(AuthMicroappImpl.InternalRoutes.ARG_MOBILE_ENABLED)!!.toBoolean()
    private set

    var isAuthInProgress by mutableStateOf(false)
    var isCodeError by mutableStateOf(false)

    var authFlow = flow {
        coroutineScope {
            while (isPollingActive) {
                pollAuthSession().also { success ->
                    if (success) isPollingActive = false
                    emit(success)
                }

                delay(5000L)
            }
        }
    }

    fun enterCode(code: String, onSuccess: () -> Unit) {
        isCodeError = false

        if (code.isEmpty()) {
            isCodeError = true
        }

        if (isCodeError) return

        viewModelScope.launch {
            isAuthInProgress = true

            if (enterCode(code)) {
                isPollingActive = false
                onSuccess()
            } else {
                isCodeError = true
            }

            isAuthInProgress = false
        }
    }

    override fun onCleared() {
        isPollingActive = false
    }
}
package bruhcollective.itaysonlab.microapp.guard.ui.qrsign

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.toComposeRect
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.jetisteam.util.Regexes
import bruhcollective.itaysonlab.ksteam.guard.models.AwaitingSession
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.ksteam.handlers.guardManagement
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QrSignScreenViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val steamClient: HostSteamClient
) : ViewModel() {
    val steamId = savedStateHandle.getSteamId()

    private val guardInstance = steamClient.client.guard.instanceFor(steamId) ?: error("GuardInstance is not available for use $steamId")
    val username = guardInstance.username

    private var toDetectedJob: Job? = null
    private var toUndetectedJob: Job? = null
    private var finishJob: Job? = null

    var state by mutableStateOf(QrState.NotDetected)
        private set

    var lastScannedRect by mutableStateOf(Rect(0f, 0f, 0f, 0f))
        private set

    var qrData by mutableStateOf<Regexes.QrData?>(null)
        private set

    var qrSessionInfo by mutableStateOf<AwaitingSession?>(null)
        private set

    var isProcessingLogin by mutableStateOf(LoginProcessState.Idle)
        private set

    var currentOperation by mutableStateOf(CurrentOperation.None)
        private set

    fun handleScannedBarcode(result: Barcode?) {
        if (state == QrState.Detected || finishJob != null) return

        if (result != null) {
            lastScannedRect = result.boundingBox!!.toComposeRect()

            if (state == QrState.NotDetected && Regexes.qrMatches(result.rawValue ?: "")) {
                state = QrState.Preheat

                toUndetectedJob?.cancel()
                toUndetectedJob = null

                if (toDetectedJob == null) {
                    toDetectedJob = viewModelScope.launch {
                        delay(500L)
                        isProcessingLogin = LoginProcessState.Idle
                        state = QrState.Detected
                        loadSessionData(result.rawValue ?: "")
                        toDetectedJob = null
                    }
                }
            }
        } else if (state == QrState.Preheat) {
            toDetectedJob?.cancel()
            toDetectedJob = null

            if (toUndetectedJob == null) {
                toUndetectedJob = viewModelScope.launch {
                    delay(250L)
                    state = QrState.NotDetected
                    toUndetectedJob = null
                }
            }
        }
    }

    private fun resetBarcode() {
        state = QrState.NotDetected
        qrSessionInfo = null
        qrData = null
    }

    private suspend fun loadSessionData(url: String) {
        qrData = Regexes.extractDataFromQr(url) ?: return
        qrSessionInfo = steamClient.client.guardManagement.getActiveSessionInfo(qrData!!.sessionId)
    }

    fun updateCurrentSignIn(allow: Boolean, invokeOnDone: (() -> Unit)?) {
        isProcessingLogin = LoginProcessState.Processing
        currentOperation = if (allow) CurrentOperation.Approve else CurrentOperation.Deny

        finishJob = viewModelScope.launch {
            steamClient.client.guardManagement.confirmNewSession(
                session = qrSessionInfo!!,
                approve = allow,
                persist = qrSessionInfo!!.requestedPersistedSession
            )

            isProcessingLogin = LoginProcessState.Success
            currentOperation = CurrentOperation.None
            qrSessionInfo = null

            delay(500L)

            resetBarcode()

            delay(1000L)

            if (invokeOnDone != null) {
                withContext(Dispatchers.Main) {
                    invokeOnDone()
                }
            }

            finishJob = null
        }
    }

    enum class LoginProcessState {
        Idle, Processing, Success
    }

    enum class CurrentOperation {
        None, Approve, Deny
    }
}
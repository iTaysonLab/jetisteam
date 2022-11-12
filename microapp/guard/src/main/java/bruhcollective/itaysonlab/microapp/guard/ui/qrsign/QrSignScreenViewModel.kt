package bruhcollective.itaysonlab.microapp.guard.ui.qrsign

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.toComposeRect
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.usecases.twofactor.GetFutureAuthSession
import bruhcollective.itaysonlab.jetisteam.usecases.twofactor.UpdateSessionWithMobileAuth
import bruhcollective.itaysonlab.jetisteam.util.Regexes
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.guard.GuardMicroappImpl
import bruhcollective.itaysonlab.microapp.guard.core.GuardController
import com.google.mlkit.vision.barcode.common.Barcode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import steam.auth.CAuthentication_GetAuthSessionInfo_Response
import steam.auth.ESessionPersistence
import javax.inject.Inject

@HiltViewModel
class QrSignScreenViewModel @Inject constructor(
    private val getFutureAuthSession: GetFutureAuthSession,
    private val updateSessionWithMobileAuth: UpdateSessionWithMobileAuth,
    savedStateHandle: SavedStateHandle,
    guardController: GuardController
): ViewModel() {
    val steamId = savedStateHandle.getSteamId(GuardMicroappImpl.InternalRoutes.ARG_STEAM_ID)
    private val guardInstance = guardController.getInstance(steamId) ?: error("GuardInstance is not available for use ${steamId.steamId}")
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

    var qrSessionInfo by mutableStateOf<CAuthentication_GetAuthSessionInfo_Response?>(null)
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
        qrSessionInfo = getFutureAuthSession(qrData!!.sessionId)
    }

    fun updateCurrentSignIn(allow: Boolean, invokeOnDone: () -> Unit) {
        isProcessingLogin = LoginProcessState.Processing
        currentOperation = if (allow) CurrentOperation.Approve else CurrentOperation.Deny

        finishJob = viewModelScope.launch {
            val version = qrData!!.version
            val clientId = qrData!!.sessionId

            updateSessionWithMobileAuth(
                version = version,
                clientId = clientId,
                steamId = steamId.steamId,
                allow = allow,
                signature = guardInstance.sgCreateSignature(version, clientId, steamId),
                persistence = qrSessionInfo?.requested_persistence ?: ESessionPersistence.k_ESessionPersistence_Ephemeral
            )

            isProcessingLogin = LoginProcessState.Success
            currentOperation = CurrentOperation.None
            qrSessionInfo = null

            delay(500L)

            resetBarcode()

            delay(1000L)

            withContext(Dispatchers.Main) {
                invokeOnDone()
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
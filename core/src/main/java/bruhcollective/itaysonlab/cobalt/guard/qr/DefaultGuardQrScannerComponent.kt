package bruhcollective.itaysonlab.cobalt.guard.qr

import bruhcollective.itaysonlab.ksteam.ExtendedSteamClient
import bruhcollective.itaysonlab.ksteam.models.SteamId
import bruhcollective.itaysonlab.ksteam.util.SteamRegexes
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class DefaultGuardQrScannerComponent (
    componentContext: ComponentContext,
    steamId: SteamId,
    private val onDismiss: () -> Unit
): GuardQrScannerComponent, KoinComponent, ComponentContext by componentContext, CoroutineScope by componentContext.coroutineScope() {
    private val steamClient: ExtendedSteamClient = get()
    private var backListener: (() -> Unit)? = null

    private var toDetectedJob: Job? = null
    private var toUndetectedJob: Job? = null
    private var finishJob: Job? = null

    override val qrState = MutableValue(GuardQrScannerComponent.QrViewfinderState.NotDetected)
    override val scannedSession = MutableValue<GuardQrScannerComponent.ScannedSession>(GuardQrScannerComponent.ScannedSession.Loading)
    override val actionInProgress = MutableValue(GuardQrScannerComponent.Action.None)

    private var isQrVisible: Boolean = false

    init {
        lifecycle.doOnDestroy {
            toDetectedJob?.cancel()
            toUndetectedJob?.cancel()
            finishJob?.cancel()

            toDetectedJob = null
            toUndetectedJob = null
            finishJob = null
        }
    }

    override fun submitQrContent(rawValue: String) {
        if (qrState.value == GuardQrScannerComponent.QrViewfinderState.Detected || finishJob != null) return

        if (qrState.value == GuardQrScannerComponent.QrViewfinderState.Preheat) {
            isQrVisible = true
            toUndetectedJob?.cancel()
            toUndetectedJob = null
        } else if (qrState.value == GuardQrScannerComponent.QrViewfinderState.NotDetected && SteamRegexes.isAuthorizationLink(rawValue)) {
            qrState.value = GuardQrScannerComponent.QrViewfinderState.Preheat

            isQrVisible = true
            toUndetectedJob?.cancel()
            toUndetectedJob = null

            if (toDetectedJob == null) {
                toDetectedJob = launch {
                    delay(500L)

                    if (isQrVisible) {
                        scannedSession.value = GuardQrScannerComponent.ScannedSession.Loading
                        qrState.value = GuardQrScannerComponent.QrViewfinderState.Detected

                        scannedSession.value = SteamRegexes.extractAuthorizationLinkDataOrNull(rawValue)?.let { data ->
                            steamClient.guardManagement.getIncomingSessionInfo(id = data.sessionId.toLong())
                        }?.let { incomingSession ->
                            GuardQrScannerComponent.ScannedSession.Found(session = incomingSession)
                        } ?: GuardQrScannerComponent.ScannedSession.Loading
                    } else {
                        qrState.value = GuardQrScannerComponent.QrViewfinderState.NotDetected
                    }

                    toDetectedJob = null
                }
            }
        }
    }

    override fun submitQrEmpty() {
        if (qrState.value == GuardQrScannerComponent.QrViewfinderState.Detected || finishJob != null) return

        if (qrState.value == GuardQrScannerComponent.QrViewfinderState.Preheat) {
            isQrVisible = false
        }
    }

    override fun approveScannedSession() {
        launchFinishJob {
            actionInProgress.value = GuardQrScannerComponent.Action.Approve

            (scannedSession.value as? GuardQrScannerComponent.ScannedSession.Found)?.let { foundSession ->
                steamClient.guardManagement.approveIncomingSession(
                    session = foundSession.session,
                    persist = foundSession.session.requestedPersistedSession
                )
            }
        }
    }

    override fun denyScannedSession() {
        launchFinishJob {
            actionInProgress.value = GuardQrScannerComponent.Action.Deny

            (scannedSession.value as? GuardQrScannerComponent.ScannedSession.Found)?.let { foundSession ->
                steamClient.guardManagement.rejectIncomingSession(
                    session = foundSession.session
                )
            }
        }
    }

    private fun launchFinishJob(func: suspend () -> Unit) {
        launch {
            func()
            scannedSession.value = GuardQrScannerComponent.ScannedSession.ActionComplete
            delay(750L)

            if (backListener != null) {
                backListener?.invoke()
            } else {
                dismiss()
            }
        }.also { job ->
            finishJob = job
        }.invokeOnCompletion {
            finishJob = null
        }
    }

    override fun dismiss() {
        onDismiss()
    }

    override fun registerBackListener(listener: () -> Unit) {
        backListener = listener
    }
}
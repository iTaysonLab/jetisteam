package bruhcollective.itaysonlab.cobalt.guard.qr

import bruhcollective.itaysonlab.ksteam.models.SteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnDestroy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

internal class DefaultGuardQrScannerComponent (
    componentContext: ComponentContext,
    steamId: SteamId,
    private val onDismiss: () -> Unit
): GuardQrScannerComponent, KoinComponent, ComponentContext by componentContext, CoroutineScope by componentContext.coroutineScope() {
    private var toDetectedJob: Job? = null
    private var toUndetectedJob: Job? = null
    private var finishJob: Job? = null

    override val qrState = MutableValue(GuardQrScannerComponent.QrViewfinderState.NotDetected)
    override val scannedSession = MutableValue<GuardQrScannerComponent.ScannedSession>(GuardQrScannerComponent.ScannedSession.Loading)

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

        if (qrState.value == GuardQrScannerComponent.QrViewfinderState.NotDetected && Regexes.qrMatches(result.rawValue ?: "")) {
            qrState.value = GuardQrScannerComponent.QrViewfinderState.Preheat

            toUndetectedJob?.cancel()
            toUndetectedJob = null

            if (toDetectedJob == null) {
                toDetectedJob = launch {
                    delay(500L)

                    scannedSession.value = GuardQrScannerComponent.ScannedSession.Loading
                    qrState.value = GuardQrScannerComponent.QrViewfinderState.Detected
                    // loadSessionData(result.rawValue ?: "")

                    toDetectedJob = null
                }
            }
        }
    }

    override fun submitQrEmpty() {
        if (qrState.value == GuardQrScannerComponent.QrViewfinderState.Detected || finishJob != null) return

        if (qrState.value == GuardQrScannerComponent.QrViewfinderState.Preheat) {
            toDetectedJob?.cancel()
            toDetectedJob = null

            if (toUndetectedJob == null) {
                toUndetectedJob = launch {
                    delay(250L)
                    qrState.value = GuardQrScannerComponent.QrViewfinderState.NotDetected
                    toUndetectedJob = null
                }
            }
        }
    }

    override fun dismiss() {
        onDismiss()
    }
}
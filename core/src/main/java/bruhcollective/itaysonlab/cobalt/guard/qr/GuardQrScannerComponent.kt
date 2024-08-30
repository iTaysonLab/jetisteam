package bruhcollective.itaysonlab.cobalt.guard.qr

import bruhcollective.itaysonlab.ksteam.guard.models.IncomingSession
import com.arkivanov.decompose.value.Value

interface GuardQrScannerComponent {
    val qrState: Value<QrViewfinderState>
    val scannedSession: Value<ScannedSession>
    val actionInProgress: Value<Action>

    fun submitQrContent(rawValue: String)
    fun submitQrEmpty()

    fun approveScannedSession()
    fun denyScannedSession()

    fun dismiss()
    fun registerBackListener(listener: () -> Unit)

    enum class QrViewfinderState {
        // Viewfinder is in default state
        NotDetected,

        // Viewfinder focuses on QR code
        Preheat,

        // Viewfinder returns to default state and expands internal content
        // Also locks the state (other detected QR codes are ignored)
        Detected
    }

    enum class Action {
        None, Approve, Deny
    }

    sealed interface ScannedSession {
        data class Found (
            val session: IncomingSession
        ): ScannedSession

        data object ActionComplete: ScannedSession

        data object Loading: ScannedSession
    }
}
package bruhcollective.itaysonlab.cobalt.guard.instance

import bruhcollective.itaysonlab.cobalt.guard.qr.GuardQrScannerComponent
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow

interface GuardInstanceComponent {
    val qrSlot: Value<ChildSlot<*, QrCodeScannerChild>>
    val state: StateFlow<GuardInstanceState>

    fun openQrScanner()

    class QrCodeScannerChild (
        val component: GuardQrScannerComponent
    )
}
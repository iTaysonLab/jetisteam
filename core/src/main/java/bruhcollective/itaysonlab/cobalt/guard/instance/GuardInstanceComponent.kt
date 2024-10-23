package bruhcollective.itaysonlab.cobalt.guard.instance

import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardIncomingSessionComponent
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardRecoveryCodeSheetComponent
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardRemoveSheetComponent
import bruhcollective.itaysonlab.cobalt.guard.instance.code.GuardCodeComponent
import bruhcollective.itaysonlab.cobalt.guard.instance.confirmations.GuardConfirmationsComponent
import bruhcollective.itaysonlab.cobalt.guard.instance.sessions.GuardSessionsComponent
import bruhcollective.itaysonlab.cobalt.guard.qr.GuardQrScannerComponent
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value

interface GuardInstanceComponent {
    val pages: Value<ChildPages<*, PageChild>>
    val alertSlot: Value<ChildSlot<*, AlertChild>>

    fun selectPage(index: Int)
    fun openQrScanner()

    // TODO: remake into something EventBus-style
    fun notifySessionsRefresh()
    fun notifyConfirmationsRefresh()

    sealed interface PageChild {
        class Code (
            val component: GuardCodeComponent
        ): PageChild

        class Confirmations (
            val component: GuardConfirmationsComponent
        ): PageChild

        class Sessions (
            val component: GuardSessionsComponent
        ): PageChild
    }

    sealed interface AlertChild {
        class RecoveryCode (
            val component: GuardRecoveryCodeSheetComponent
        ): AlertChild

        class DeleteGuard (
            val component: GuardRemoveSheetComponent
        ): AlertChild

        class QrCodeScanner (
            val component: GuardQrScannerComponent
        ): AlertChild

        class IncomingSession (
            val component: GuardIncomingSessionComponent
        ): AlertChild
    }
}
package bruhcollective.itaysonlab.cobalt.guard.instance

import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardRecoveryCodeSheetComponent
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardRemoveSheetComponent
import bruhcollective.itaysonlab.cobalt.guard.confirmation.GuardConfirmationComponent
import bruhcollective.itaysonlab.cobalt.guard.qr.GuardQrScannerComponent
import bruhcollective.itaysonlab.cobalt.guard.session.GuardSessionDetailComponent
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.StateFlow

interface GuardInstanceComponent {
    val fullscreenAlertSlot: Value<ChildSlot<*, FullscreenAlertChild>>
    val alertSlot: Value<ChildSlot<*, AlertChild>>

    val state: StateFlow<GuardInstanceState>

    fun openQrScanner()
    fun openSessionDetail(session: ActiveSession)

    fun openRecoveryCodeSheet()
    fun openDeleteSheet()

    sealed interface FullscreenAlertChild {
        class QrCodeScanner (
            val component: GuardQrScannerComponent
        ): FullscreenAlertChild

        class MobileConfirmationDetail (
            val component: GuardConfirmationComponent
        ): FullscreenAlertChild

        class SessionDetail (
            val component: GuardSessionDetailComponent
        ): FullscreenAlertChild
    }

    sealed interface AlertChild {
        class RecoveryCode (
            val component: GuardRecoveryCodeSheetComponent
        ): AlertChild

        class DeleteGuard (
            val component: GuardRemoveSheetComponent
        ): AlertChild
    }
}
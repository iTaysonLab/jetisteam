package bruhcollective.itaysonlab.cobalt.guard

import bruhcollective.itaysonlab.cobalt.core.decompose.HandlesScrollToTopChild
import bruhcollective.itaysonlab.cobalt.core.decompose.HandlesScrollToTopComponent
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardRecoveryCodeSheetComponent
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardRemoveSheetComponent
import bruhcollective.itaysonlab.cobalt.guard.confirmation.GuardConfirmationComponent
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardIncomingSessionComponent
import bruhcollective.itaysonlab.cobalt.guard.instance.GuardInstanceComponent
import bruhcollective.itaysonlab.cobalt.guard.qr.GuardQrScannerComponent
import bruhcollective.itaysonlab.cobalt.guard.session.GuardSessionDetailComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.alert.GuardAlreadyExistsAlertComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.onboarding.GuardOnboardingComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.recovery.GuardRecoveryCodeComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.sms.GuardEnterSmsComponent
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner

interface GuardComponent: BackHandlerOwner, HandlesScrollToTopComponent {
    val stack: Value<ChildStack<*, Child>>
    val alertSlot: Value<ChildSlot<*, AlertChild>>

    fun onBackPressed()

    sealed interface Child {
        // Setup

        class SetupOnboarding (
            val component: GuardOnboardingComponent
        ) : Child

        class SetupEnterSmsCode (
            val component: GuardEnterSmsComponent
        ): Child

        class SetupRecoveryCode (
            val component: GuardRecoveryCodeComponent
        ): Child

        // Instance

        class Instance (
            val component: GuardInstanceComponent
        ): Child, HandlesScrollToTopChild {
            override fun scrollToTop() = component.scrollToTop()
        }

        class ActiveSessionDetail (
            val component: GuardSessionDetailComponent
        ): Child

        class MobileConfirmationDetail (
            val component: GuardConfirmationComponent
        ): Child
    }

    sealed interface AlertChild {
        class SetupOverrideExisting(
            val component: GuardAlreadyExistsAlertComponent
        ): AlertChild

        class InstanceRecoveryCode (
            val component: GuardRecoveryCodeSheetComponent
        ): AlertChild

        class InstanceDelete (
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
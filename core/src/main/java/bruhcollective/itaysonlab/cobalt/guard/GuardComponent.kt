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
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackHandlerOwner

interface GuardComponent {
    val slot: Value<ChildSlot<*, Child>>

    fun notifySessionsUpdated()
    fun notifyConfirmationsUpdated()

    sealed interface Child {
        class Onboarding (
            val component: GuardOnboardingComponent
        ): Child

        class Instance (
            val component: GuardInstanceComponent
        ): Child
    }
}
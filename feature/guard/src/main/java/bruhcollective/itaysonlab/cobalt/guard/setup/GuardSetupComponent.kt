package bruhcollective.itaysonlab.cobalt.guard.setup

import bruhcollective.itaysonlab.cobalt.guard.setup.alert.GuardAlreadyExistsAlertComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.onboarding.GuardOnboardingComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.recovery.GuardRecoveryCodeComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.sms.GuardEnterSmsComponent
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface GuardSetupComponent {
    val alert: Value<ChildSlot<*, AlertChild>>
    val childStack: Value<ChildStack<*, Child>>

    fun onAlertDismissed()

    sealed interface Child {
        class Onboarding(val component: GuardOnboardingComponent) : Child
        class EnterSmsCode(val component: GuardEnterSmsComponent) : Child
        class SaveRecoveryCode(val component: GuardRecoveryCodeComponent) : Child
    }

    sealed interface AlertChild {
        class GuardAlreadyExists(
            val component: GuardAlreadyExistsAlertComponent
        ): AlertChild
    }
}
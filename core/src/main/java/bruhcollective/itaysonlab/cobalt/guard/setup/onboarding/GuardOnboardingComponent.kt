package bruhcollective.itaysonlab.cobalt.guard.setup.onboarding

import bruhcollective.itaysonlab.cobalt.guard.setup.alert.GuardAlreadyExistsAlertComponent
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value

interface GuardOnboardingComponent {
    val alertSlot: Value<ChildSlot<*, AlertChild>>
    val isTryingToStartSetup: Value<Boolean>

    fun onSetupClicked()

    sealed interface AlertChild {
        class OverrideExisting(
            val component: GuardAlreadyExistsAlertComponent
        ) : AlertChild
    }
}
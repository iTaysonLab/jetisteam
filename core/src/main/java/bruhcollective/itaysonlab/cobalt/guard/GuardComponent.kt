package bruhcollective.itaysonlab.cobalt.guard

import bruhcollective.itaysonlab.cobalt.guard.instance.GuardInstanceComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.onboarding.GuardOnboardingComponent
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value

interface GuardComponent {
    val slot: Value<ChildSlot<*, Child>>

    fun notifySessionsUpdated()
    fun notifyConfirmationsUpdated()
    fun notifySlotUpdate()

    sealed interface Child {
        class Onboarding (
            val component: GuardOnboardingComponent
        ): Child

        class Instance (
            val component: GuardInstanceComponent
        ): Child
    }
}
package bruhcollective.itaysonlab.cobalt.guard

import bruhcollective.itaysonlab.cobalt.guard.instance.GuardInstanceComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.GuardSetupComponent
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value

interface GuardComponent {
    val slot: Value<ChildSlot<*, Child>>

    sealed interface Child {
        class Setup (val component: GuardSetupComponent) : Child
        class InstanceReady (val component: GuardInstanceComponent) : Child
    }
}
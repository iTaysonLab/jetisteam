package bruhcollective.itaysonlab.cobalt.guard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import bruhcollective.itaysonlab.cobalt.guard.instance.GuardInstanceScreen
import bruhcollective.itaysonlab.cobalt.guard.setup.onboarding.GuardOnboardingScreen
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun GuardRootScreen(
    component: GuardComponent
) {
    val slot by component.slot.subscribeAsState()

    slot.child?.instance?.let {
        when (val child = it) {
            is GuardComponent.Child.Instance -> {
                GuardInstanceScreen(child.component)
            }

            is GuardComponent.Child.Onboarding -> {
                GuardOnboardingScreen(child.component)
            }
        }
    }
}
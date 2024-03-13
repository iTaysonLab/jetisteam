package bruhcollective.itaysonlab.jetisteam.guard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.Dp
import bruhcollective.itaysonlab.cobalt.guard.GuardComponent
import bruhcollective.itaysonlab.jetisteam.guard.instance.GuardInstanceScreen
import bruhcollective.itaysonlab.jetisteam.guard.setup.GuardSetupScreen
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun GuardScreen(
    component: GuardComponent,
    topPadding: Dp
) {
    val slot by component.slot.subscribeAsState()

    slot.child?.instance?.let {
        when (val child = it) {
            is GuardComponent.Child.Setup -> GuardSetupScreen(child.component, topPadding)
            is GuardComponent.Child.InstanceReady -> GuardInstanceScreen(child.component, topPadding)
        }
    }
}
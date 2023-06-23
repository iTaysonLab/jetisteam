package bruhcollective.itaysonlab.jetisteam.guard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.Dp
import bruhcollective.itaysonlab.cobalt.guard.GuardComponent
import bruhcollective.itaysonlab.cobalt.signin.SignRootComponent
import bruhcollective.itaysonlab.jetisteam.AndroidCobaltComponent
import bruhcollective.itaysonlab.jetisteam.guard.setup.GuardSetupScreen
import bruhcollective.itaysonlab.jetisteam.navigation.CobaltContainerScreen
import bruhcollective.itaysonlab.jetisteam.signin.SignInScreen
import bruhcollective.itaysonlab.jetisteam.signin.pages.AuthScreen
import bruhcollective.itaysonlab.jetisteam.signin.pages.TwoFactorScreen
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState

@Composable
fun GuardScreen(
    component: GuardComponent,
    topPadding: Dp
) {
    val slot by component.slot.subscribeAsState()

    slot.child?.instance?.let {
        when (val child = it) {
            is GuardComponent.Child.Setup -> GuardSetupScreen(child.component, topPadding)
            is GuardComponent.Child.InstanceReady -> TODO()
        }
    }
}
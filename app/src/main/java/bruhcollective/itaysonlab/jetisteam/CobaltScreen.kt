package bruhcollective.itaysonlab.jetisteam

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import bruhcollective.itaysonlab.jetisteam.navigation.CobaltContainerScreen
import bruhcollective.itaysonlab.jetisteam.signin.SignInScreen
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState

@Composable
fun CobaltScreen(
    isConnectionRowShown: Boolean,
    component: AndroidCobaltComponent
) {
    val slot by component.slot.subscribeAsState()

    slot.child?.instance?.let {
        when (val child = it) {
            is AndroidCobaltComponent.Slot.SignIn -> SignInScreen(child.component)
            is AndroidCobaltComponent.Slot.Cobalt -> CobaltContainerScreen(isConnectionRowShown, child.component)
        }
    }
}
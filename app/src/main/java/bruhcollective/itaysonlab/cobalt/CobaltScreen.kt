package bruhcollective.itaysonlab.cobalt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import bruhcollective.itaysonlab.cobalt.navigation.CobaltContainerScreen
import bruhcollective.itaysonlab.cobalt.signin.SignInScreen
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@Composable
fun CobaltScreen(
    component: AndroidCobaltComponent
) {
    val slot by component.slot.subscribeAsState()

    slot.child?.instance?.let {
        when (val child = it) {
            is AndroidCobaltComponent.Slot.SignIn -> SignInScreen(child.component)
            is AndroidCobaltComponent.Slot.Cobalt -> CobaltContainerScreen(child.component)
        }
    }
}
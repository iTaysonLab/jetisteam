package bruhcollective.itaysonlab.cobalt

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.cobalt.navigation.RootNavigationScreen
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
            is AndroidCobaltComponent.Slot.Cobalt -> {
                val steamConnectionStatus by component.steamConnectionState.collectAsStateWithLifecycle()

                RootNavigationScreen(component = child.component, steamConnectionStatus = steamConnectionStatus)
            }
        }
    }
}
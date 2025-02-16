package bruhcollective.itaysonlab.cobalt.screens.signin

import androidx.compose.runtime.Composable
import bruhcollective.itaysonlab.cobalt.screens.signin.pages.AuthScreen
import bruhcollective.itaysonlab.cobalt.screens.signin.pages.TwoFactorScreen
import bruhcollective.itaysonlab.cobalt.signin.SignRootComponent
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation

@Composable
fun SignInScreen(
    component: SignRootComponent
) {
    Children(
        stack = component.childStack,
        animation = stackAnimation(fade() + slide())
    ) {
        when (val child = it.instance) {
            is SignRootComponent.Child.SignIn -> AuthScreen(child.component)
            is SignRootComponent.Child.TwoFactor -> TwoFactorScreen(child.component)
        }
    }
}
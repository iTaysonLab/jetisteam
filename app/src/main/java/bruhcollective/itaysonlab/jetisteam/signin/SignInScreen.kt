package bruhcollective.itaysonlab.jetisteam.signin

import androidx.compose.runtime.Composable
import bruhcollective.itaysonlab.cobalt.signin.SignRootComponent
import bruhcollective.itaysonlab.jetisteam.signin.pages.AuthScreen
import bruhcollective.itaysonlab.jetisteam.signin.pages.TwoFactorScreen
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
            is SignRootComponent.Child.WebView -> AuthScreen(child.component)
        }
    }
}
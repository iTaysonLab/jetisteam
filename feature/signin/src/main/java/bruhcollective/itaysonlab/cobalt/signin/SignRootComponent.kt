package bruhcollective.itaysonlab.cobalt.signin

import bruhcollective.itaysonlab.cobalt.signin.auth.AuthComponent
import bruhcollective.itaysonlab.cobalt.signin.tfa.TwoFactorComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface SignRootComponent {
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        class SignIn(val component: AuthComponent) : Child()
        class TwoFactor(val component: TwoFactorComponent) : Child()
        class WebView(val component: AuthComponent) : Child()
    }
}
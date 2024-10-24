package bruhcollective.itaysonlab.cobalt.signin

import bruhcollective.itaysonlab.cobalt.signin.auth.AuthComponent
import bruhcollective.itaysonlab.cobalt.signin.auth.DefaultAuthComponent
import bruhcollective.itaysonlab.cobalt.signin.tfa.DefaultTwoFactorComponent
import bruhcollective.itaysonlab.cobalt.signin.tfa.TwoFactorComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable

class DefaultSignRootComponent (
    componentContext: ComponentContext,
    private val onAuthorizationCompleted: () -> Unit,
): SignRootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, SignRootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.SignIn,
        handleBackButton = true,
        childFactory = ::createChild,
        serializer = Config.serializer()
    )

    private fun createChild(config: Config, componentContext: ComponentContext): SignRootComponent.Child {
        return when (config) {
            Config.SignIn -> SignRootComponent.Child.SignIn(signInComponent(componentContext))
            Config.TwoFactor -> SignRootComponent.Child.TwoFactor(twoFactorComponent(componentContext))
        }
    }

    private fun signInComponent(componentContext: ComponentContext): AuthComponent {
        return DefaultAuthComponent(componentContext, onProceedToTfa = {
            navigation.pushNew(Config.TwoFactor)
        })
    }

    private fun twoFactorComponent(componentContext: ComponentContext): TwoFactorComponent {
        return DefaultTwoFactorComponent(componentContext, onBack = navigation::pop, onAuthorizationCompleted = onAuthorizationCompleted)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object SignIn : Config

        @Serializable
        data object TwoFactor : Config
    }
}
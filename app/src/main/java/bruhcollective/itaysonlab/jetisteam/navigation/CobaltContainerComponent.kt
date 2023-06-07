package bruhcollective.itaysonlab.jetisteam.navigation

import bruhcollective.itaysonlab.cobalt.news.DefaultNewsRootComponent
import bruhcollective.itaysonlab.cobalt.news.NewsRootComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.parcelize.Parcelize

class CobaltContainerComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    val childStack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(config: Config, componentContext: ComponentContext): Child {
        return when (config) {
            Config.Home -> Child.Home(newsComponent(componentContext))
        }
    }

    private fun newsComponent(componentContext: ComponentContext): NewsRootComponent {
        return DefaultNewsRootComponent(componentContext)
    }

    @Parcelize
    private sealed class Config : Parcelable {
        object Home : Config()
    }

    sealed class Child {
        class Home(val component: NewsRootComponent) : Child()
    }
}
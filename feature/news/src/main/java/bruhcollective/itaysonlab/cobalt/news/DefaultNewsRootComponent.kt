package bruhcollective.itaysonlab.cobalt.news

import bruhcollective.itaysonlab.cobalt.news.discover.DiscoverComponent
import bruhcollective.itaysonlab.cobalt.news.discover.FusionDiscoverComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.parcelize.Parcelize

class DefaultNewsRootComponent (
    componentContext: ComponentContext
): NewsRootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()

    override val childStack: Value<ChildStack<*, NewsRootComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Discover,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(config: Config, componentContext: ComponentContext): NewsRootComponent.Child {
        return when (config) {
            Config.Discover -> NewsRootComponent.Child.Discover(discoverComponent(componentContext))
        }
    }

    private fun discoverComponent(componentContext: ComponentContext): DiscoverComponent {
        return FusionDiscoverComponent(componentContext)
    }

    private sealed interface Config : Parcelable {
        @Parcelize
        object Discover : Config
    }
}
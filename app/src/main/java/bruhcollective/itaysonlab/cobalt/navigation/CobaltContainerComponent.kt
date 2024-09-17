package bruhcollective.itaysonlab.cobalt.navigation

import bruhcollective.itaysonlab.cobalt.core.decompose.HandlesScrollToTopChild
import bruhcollective.itaysonlab.cobalt.core.decompose.HandlesScrollToTopComponent
import bruhcollective.itaysonlab.cobalt.core.decompose.ViewModel
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.guard.DefaultGuardComponent
import bruhcollective.itaysonlab.cobalt.guard.GuardComponent
import bruhcollective.itaysonlab.cobalt.news.DefaultNewsRootComponent
import bruhcollective.itaysonlab.cobalt.news.NewsRootComponent
import bruhcollective.itaysonlab.cobalt.profile.MyProfileComponent
import bruhcollective.itaysonlab.cobalt.profile.ProfileComponent
import bruhcollective.itaysonlab.cobalt.root_flows.DefaultRootLibraryFlowComponent
import bruhcollective.itaysonlab.cobalt.root_flows.RootLibraryFlowComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.active
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CobaltContainerComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory
): ComponentContext by componentContext, KoinComponent {
    private val steamClient: SteamClient by inject()

    private val navigation = StackNavigation<Config>()
    private val viewModel = instanceKeeper.getOrCreate { ContainerViewModel() }

    val navigationItems: Value<ImmutableList<NavigationItem>> get() = viewModel.navigationItems

    val childStack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.News,
        handleBackButton = true,
        childFactory = ::createChild,
        serializer = Config.serializer()
    )

    val currentNavigationItem: Value<NavigationItem> = childStack.map { NavigationItem.fromChild(it.active.instance) }
    val connectionStatus get() = steamClient.connectionStatus

    private fun createChild(config: Config, componentContext: ComponentContext): Child {
        return when (config) {
            Config.News -> Child.News(newsComponent(componentContext))
            Config.Guard -> Child.Guard(guardComponent(componentContext))
            Config.MyProfile -> Child.MyProfile(myProfileComponent(componentContext))
            Config.Library -> Child.Library(libraryComponent(componentContext))
        }
    }

    private fun newsComponent(componentContext: ComponentContext): NewsRootComponent {
        return DefaultNewsRootComponent(componentContext)
    }

    private fun guardComponent(componentContext: ComponentContext): GuardComponent {
        return DefaultGuardComponent(componentContext, storeFactory)
    }

    private fun myProfileComponent(componentContext: ComponentContext): ProfileComponent {
        return MyProfileComponent(componentContext)
    }

    private fun libraryComponent(componentContext: ComponentContext): RootLibraryFlowComponent {
        return DefaultRootLibraryFlowComponent(componentContext)
    }

    fun switch(to: NavigationItem) {
        if (to == currentNavigationItem.value) {
            (childStack.active.instance as? HandlesScrollToTopChild)?.let { child ->
                child.scrollToTop()
                return
            }
        }

        when (to) {
            NavigationItem.Home -> navigation.bringToFront(Config.News)
            NavigationItem.Guard -> navigation.bringToFront(Config.Guard)
            NavigationItem.MyProfile -> navigation.bringToFront(Config.MyProfile)
            NavigationItem.Library -> navigation.bringToFront(Config.Library)
        }
    }

    fun getNavigationItemIndex(item: NavigationItem): Int {
        return navigationItems.value.indexOf(item)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object News : Config

        @Serializable
        data object Guard : Config

        @Serializable
        data object Library : Config

        @Serializable
        data object MyProfile : Config
    }

    sealed interface Child {
        data class News(val component: NewsRootComponent) : Child

        data class Guard(val component: GuardComponent) : Child, HandlesScrollToTopChild {
            override fun scrollToTop() = component.scrollToTop()
        }

        data class Library(val component: RootLibraryFlowComponent) : Child, HandlesScrollToTopChild {
            override fun scrollToTop() = component.scrollToTop()
        }

        data class MyProfile(val component: ProfileComponent) : Child
    }

    enum class NavigationItem {
        Home,
        Guard,
        Library,
        MyProfile;

        companion object {
            fun fromChild(child: Child) = when (child) {
                is Child.News -> Home
                is Child.Guard -> Guard
                is Child.MyProfile -> MyProfile
                is Child.Library -> Library
            }
        }
    }

    private class ContainerViewModel: ViewModel() {
        val navigationItems = MutableValue(NavigationItem.entries.toImmutableList())
    }
}
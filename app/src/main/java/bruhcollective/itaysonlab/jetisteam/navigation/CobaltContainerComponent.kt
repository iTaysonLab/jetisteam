package bruhcollective.itaysonlab.jetisteam.navigation

import bruhcollective.itaysonlab.cobalt.core.decompose.ViewModel
import bruhcollective.itaysonlab.cobalt.guard.DefaultGuardComponent
import bruhcollective.itaysonlab.cobalt.guard.GuardComponent
import bruhcollective.itaysonlab.cobalt.news.DefaultNewsRootComponent
import bruhcollective.itaysonlab.cobalt.news.NewsRootComponent
import bruhcollective.itaysonlab.cobalt.profile.MyProfileComponent
import bruhcollective.itaysonlab.cobalt.profile.ProfileComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.parcelize.Parcelize

class CobaltContainerComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()
    private val viewModel = instanceKeeper.getOrCreate { ContainerViewModel() }

    val currentNavigationItem: Value<NavigationItem> get() = viewModel.currentNavigationItem
    val navigationItems: Value<ImmutableList<NavigationItem>> get() = viewModel.navigationItems


    val childStack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(config: Config, componentContext: ComponentContext): Child {
        return when (config) {
            Config.Home -> Child.Home(newsComponent(componentContext))
            Config.Guard -> Child.Guard(guardComponent(componentContext))
            Config.MyProfile -> Child.MyProfile(myProfileComponent(componentContext))
        }
    }

    private fun newsComponent(componentContext: ComponentContext): NewsRootComponent {
        return DefaultNewsRootComponent(componentContext)
    }

    private fun guardComponent(componentContext: ComponentContext): GuardComponent {
        return DefaultGuardComponent(componentContext)
    }

    private fun myProfileComponent(componentContext: ComponentContext): ProfileComponent {
        return MyProfileComponent(componentContext)
    }

    fun switch(to: NavigationItem) {
        viewModel.onNavigationItemChanged(to)

        when (to) {
            NavigationItem.Home -> navigation.bringToFront(Config.Home)
            NavigationItem.Guard -> navigation.bringToFront(Config.Guard)
            NavigationItem.MyProfile -> navigation.bringToFront(Config.MyProfile)
        }
    }

    fun getNavigationItemIndex(item: NavigationItem): Int {
        return navigationItems.value.indexOf(item)
    }

    private sealed interface Config : Parcelable {
        @Parcelize
        object Home : Config

        @Parcelize
        object Guard : Config

        @Parcelize
        object MyProfile : Config
    }

    sealed interface Child {
        class Home(val component: NewsRootComponent) : Child
        class Guard(val component: GuardComponent) : Child
        class MyProfile(val component: ProfileComponent) : Child
    }

    enum class NavigationItem {
        Home,
        Guard,
        MyProfile
    }

    private class ContainerViewModel: ViewModel() {
        val currentNavigationItem = MutableValue(NavigationItem.Home)
        val navigationItems = MutableValue(NavigationItem.values().toList().toImmutableList())

        fun onNavigationItemChanged(item: NavigationItem) {
            currentNavigationItem.value = item
        }
    }
}
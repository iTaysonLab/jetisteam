package bruhcollective.itaysonlab.jetisteam.navigation

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
import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import kotlinx.parcelize.Parcelize

class CobaltContainerComponent(
    componentContext: ComponentContext
): ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()
    private val _currentNavigationItem = MutableValue(NavigationItem.Home)

    val currentNavigationItem: Value<NavigationItem> = _currentNavigationItem
    val navigationItems: Value<ImmutableList<NavigationItem>> = MutableValue(NavigationItem.values().toList().toImmutableList())

    val childStack: Value<ChildStack<*, Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Home,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(config: Config, componentContext: ComponentContext): Child {
        return when (config) {
            Config.Home -> Child.Home(newsComponent(componentContext))
            Config.MyProfile -> Child.MyProfile(myProfileComponent(componentContext))
        }
    }

    private fun newsComponent(componentContext: ComponentContext): NewsRootComponent {
        return DefaultNewsRootComponent(componentContext)
    }

    private fun myProfileComponent(componentContext: ComponentContext): ProfileComponent {
        return MyProfileComponent(componentContext)
    }

    fun switch(to: NavigationItem) {
        _currentNavigationItem.value = to

        when (to) {
            NavigationItem.Home -> navigation.bringToFront(Config.Home)
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
        object MyProfile : Config
    }

    sealed interface Child {
        class Home(val component: NewsRootComponent) : Child
        class MyProfile(val component: ProfileComponent) : Child
    }

    enum class NavigationItem {
        Home,
        MyProfile
    }
}
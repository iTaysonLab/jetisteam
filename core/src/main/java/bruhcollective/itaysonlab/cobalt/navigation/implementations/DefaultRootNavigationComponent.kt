package bruhcollective.itaysonlab.cobalt.navigation.implementations

import bruhcollective.itaysonlab.cobalt.navigation.DefaultDestinationComponent
import bruhcollective.itaysonlab.cobalt.navigation.DestinationComponent
import bruhcollective.itaysonlab.cobalt.navigation.DestinationRoute
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

class DefaultRootNavigationComponent (
    componentContext: ComponentContext
): RootNavigationComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<RootDestination>()

    override val availableRootDestinations: Value<ImmutableList<RootDestination>> = MutableValue(
        persistentListOf(RootDestination.NEWSFEED, RootDestination.GUARD, RootDestination.LIBRARY, RootDestination.PROFILE)
    )

    override val stack: Value<ChildStack<RootDestination, DestinationComponent>> = childStack(
        source = navigation,
        initialStack = { listOf(RootDestination.NEWSFEED) },
        childFactory = ::createChild,
        serializer = RootDestination.serializer(),
        handleBackButton = false
    )

    private fun createChild(configuration: RootDestination, componentContext: ComponentContext): DestinationComponent {
        val initialRoute = when (configuration) {
            RootDestination.NEWSFEED -> DestinationRoute.Newsfeed
            RootDestination.GUARD -> DestinationRoute.Guard
            RootDestination.LIBRARY -> DestinationRoute.Library
            RootDestination.PROFILE -> DestinationRoute.MyProfile
        }

        return DefaultDestinationComponent(initialDestinationRoute = initialRoute, componentContext = componentContext)
    }

    override fun selectRootDestination(value: RootDestination) {
        /**
         * if (to == currentNavigationItem.value) {
         *             (childStack.active.instance as? HandlesScrollToTopChild)?.let { child ->
         *                 child.scrollToTop()
         *                 return
         *             }
         *         }
         */

        navigation.bringToFront(value)
    }
}
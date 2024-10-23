package bruhcollective.itaysonlab.cobalt.navigation.implementations

import bruhcollective.itaysonlab.cobalt.navigation.DestinationComponent
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList

/**
 * Defines a root navigation component - a "scaffold" that contains the destination (screen stack) and a navigation primitive (bottom bar).
 */
interface RootNavigationComponent {
    val stack: Value<ChildStack<RootDestination, DestinationComponent>>

    val availableRootDestinations: Value<ImmutableList<RootDestination>>

    fun selectRootDestination(value: RootDestination)
}
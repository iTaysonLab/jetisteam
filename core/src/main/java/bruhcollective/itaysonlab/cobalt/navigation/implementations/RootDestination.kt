package bruhcollective.itaysonlab.cobalt.navigation.implementations

import kotlinx.serialization.Serializable

/**
 * Defines a root destination (an item in the bottom navigation bar).
 */
@Serializable
sealed interface RootDestination {
    /**
     * Default 0: Newsfeed
     */
    data object Newsfeed: RootDestination

    /**
     * Default 1: Guard
     */
    data object Guard: RootDestination

    /**
     * Default 2: Library
     */
    data object Library: RootDestination

    /**
     * Default 3: Profile
     * TODO: remove it to the top bar
     */
    data object Profile: RootDestination
}
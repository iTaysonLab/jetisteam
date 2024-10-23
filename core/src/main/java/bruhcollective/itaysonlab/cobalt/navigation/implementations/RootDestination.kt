package bruhcollective.itaysonlab.cobalt.navigation.implementations

import kotlinx.serialization.Serializable

/**
 * Defines a root destination (an item in the bottom navigation bar).
 */
@Serializable
enum class RootDestination {
    /**
     * Default 0: Newsfeed
     */
    NEWSFEED,

    /**
     * Default 1: Guard
     */
    GUARD,

    /**
     * Default 2: Library
     */
    LIBRARY,

    /**
     * Default 3: Profile
     * TODO: remove it to the top bar
     */
    PROFILE
}
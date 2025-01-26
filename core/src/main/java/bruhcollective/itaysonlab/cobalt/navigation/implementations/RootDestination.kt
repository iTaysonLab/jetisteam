package bruhcollective.itaysonlab.cobalt.navigation.implementations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Defines a root destination (an item in the bottom navigation bar).
 */
@Serializable
sealed interface RootDestination {
    /**
     * Default 0: Newsfeed
     */
    @SerialName("newsfeed")
    @Serializable
    data object Newsfeed: RootDestination

    /**
     * Default 1: Guard
     */
    @SerialName("guard")
    @Serializable
    data object Guard: RootDestination

    /**
     * Default 2: Library
     */
    @SerialName("library")
    @Serializable
    data object Library: RootDestination

    /**
     * Default 3: Profile
     * TODO: remove it to the top bar
     */
    @SerialName("profile")
    @Serializable
    data object Profile: RootDestination
}
package bruhcollective.itaysonlab.cobalt.navigation.implementations

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Defines a root destination (an item in the bottom navigation bar).
 */
@Serializable
sealed interface RootDestination {
    val index: Int

    /**
     * Default 0: Newsfeed
     */
    @SerialName("newsfeed")
    @Serializable
    data object Newsfeed: RootDestination {
        override val index: Int = 0
    }

    /**
     * Default 1: Guard
     */
    @SerialName("guard")
    @Serializable
    data object Guard: RootDestination {
        override val index: Int = 1
    }

    /**
     * Default 2: Library
     */
    @SerialName("library")
    @Serializable
    data object Library: RootDestination {
        override val index: Int = 2
    }

    /**
     * Default 3: Profile
     * TODO: remove it to the top bar/sheet
     */
    @SerialName("profile")
    @Serializable
    data object Profile: RootDestination {
        override val index: Int = 3
    }
}
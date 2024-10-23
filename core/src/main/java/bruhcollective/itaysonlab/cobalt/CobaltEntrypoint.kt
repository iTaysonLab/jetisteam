package bruhcollective.itaysonlab.cobalt

import bruhcollective.itaysonlab.cobalt.navigation.DefaultDestinationComponent
import bruhcollective.itaysonlab.cobalt.navigation.DestinationComponent
import bruhcollective.itaysonlab.cobalt.navigation.DestinationRoute
import bruhcollective.itaysonlab.cobalt.navigation.implementations.DefaultRootNavigationComponent
import bruhcollective.itaysonlab.cobalt.navigation.implementations.RootNavigationComponent
import com.arkivanov.decompose.ComponentContext

object CobaltEntrypoint {
    fun rootComponent(
        componentContext: ComponentContext
    ): RootNavigationComponent {
        return DefaultRootNavigationComponent(
            componentContext = componentContext
        )
    }

    fun destinationComponent(
        initialDestination: DestinationRoute,
        componentContext: ComponentContext
    ): DestinationComponent {
        return DefaultDestinationComponent(
            initialDestinationRoute = initialDestination,
            componentContext = componentContext
        )
    }
}
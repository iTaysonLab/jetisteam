package bruhcollective.itaysonlab.cobalt

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
}
package bruhcollective.itaysonlab.cobalt

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.navigation.implementations.RootNavigationComponent
import bruhcollective.itaysonlab.cobalt.signin.DefaultSignRootComponent
import bruhcollective.itaysonlab.cobalt.signin.SignRootComponent
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AndroidCobaltComponent (
    componentContext: ComponentContext
): ComponentContext by componentContext, KoinComponent {
    private val steamClient: SteamClient by inject()

    private val slotNavigation = SlotNavigation<Config>()
    val steamConnectionState get() = steamClient.connectionStatus

    val slot: Value<ChildSlot<*, Slot>> = childSlot(
        source = slotNavigation,
        handleBackButton = false,
        childFactory = ::createSlot,
        initialConfiguration = ::createInitialConfiguration,
        serializer = Config.serializer()
    )

    private fun createSlot(config: Config, componentContext: ComponentContext): Slot {
        return when (config) {
            Config.SignIn -> Slot.SignIn(DefaultSignRootComponent(componentContext, onAuthorizationCompleted = this::onAuthorizationCompleted))
            Config.Cobalt -> Slot.Cobalt(CobaltEntrypoint.rootComponent(componentContext))
        }
    }

    private fun createInitialConfiguration(): Config {
        return if (steamClient.ksteam.account.hasSavedDataForAtLeastOneAccount()) {
            Config.Cobalt
        } else {
            Config.SignIn
        }
    }

    private fun onAuthorizationCompleted() {
        slotNavigation.activate(Config.Cobalt)
    }

    sealed interface Slot {
        class SignIn(val component: SignRootComponent) : Slot
        class Cobalt(val component: RootNavigationComponent) : Slot
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object SignIn : Config

        @Serializable
        data object Cobalt : Config
    }
}
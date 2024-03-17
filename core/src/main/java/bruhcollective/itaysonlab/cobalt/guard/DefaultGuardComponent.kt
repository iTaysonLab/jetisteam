package bruhcollective.itaysonlab.cobalt.guard

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.guard.instance.DefaultGuardInstanceComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.DefaultGuardSetupComponent
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.ksteam.models.toSteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DefaultGuardComponent (
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory
): GuardComponent, ComponentContext by componentContext, KoinComponent {
    private val steamClient: SteamClient by inject()
    private val navigation = SlotNavigation<Config>()

    override val slot: Value<ChildSlot<*, GuardComponent.Child>> = childSlot(
        source = navigation,
        handleBackButton = false,
        childFactory = ::createSlot,
        initialConfiguration = ::createInitialConfiguration,
        serializer = Config.serializer()
    )

    private fun createSlot(config: Config, componentContext: ComponentContext): GuardComponent.Child {
        return when (config) {
            is Config.Ready -> {
                GuardComponent.Child.InstanceReady(
                    component = DefaultGuardInstanceComponent(
                        componentContext = componentContext,
                        storeFactory = storeFactory,
                        steamId = config.steamId.toSteamId(),
                        onGuardRemovedSuccessfully = ::navigateToGuardSetup
                    )
                )
            }

            Config.Setup -> {
                GuardComponent.Child.Setup(
                    component = DefaultGuardSetupComponent(
                        componentContext = componentContext,
                        onSuccess = ::navigateToCurrentGuard
                    )
                )
            }
        }
    }

    private fun navigateToGuardSetup() {
        navigation.activate(Config.Setup)
    }

    private fun navigateToCurrentGuard() {
        navigation.activate(Config.Ready(steamId = steamClient.currentSteamId.id))
    }

    private fun createInitialConfiguration(): Config {
        return steamClient.ksteam.guard.instanceForCurrentUser()?.let { instance ->
            Config.Ready(instance.steamId.id)
        } ?: Config.Setup
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Setup : Config

        @Serializable
        data class Ready(
            val steamId: ULong
        ) : Config
    }
}
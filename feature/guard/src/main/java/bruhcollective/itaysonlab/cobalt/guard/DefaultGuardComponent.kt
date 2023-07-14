package bruhcollective.itaysonlab.cobalt.guard

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.guard.setup.DefaultGuardSetupComponent
import bruhcollective.itaysonlab.ksteam.handlers.guard
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.parcelize.Parcelize
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DefaultGuardComponent (
    componentContext: ComponentContext
): GuardComponent, ComponentContext by componentContext, KoinComponent {
    private val steamClient: SteamClient by inject()
    private val navigation = SlotNavigation<Config>()

    override val slot: Value<ChildSlot<*, GuardComponent.Child>> = childSlot(
        source = navigation,
        handleBackButton = false,
        childFactory = ::createSlot,
        initialConfiguration = ::createInitialConfiguration
    )

    private fun createSlot(config: Config, componentContext: ComponentContext): GuardComponent.Child {
        return when (config) {
            is Config.Ready -> GuardComponent.Child.InstanceReady(TODO())
            Config.Setup -> GuardComponent.Child.Setup(DefaultGuardSetupComponent(onSuccess = ::navigateToCurrentGuard, componentContext))
        }
    }

    private fun navigateToCurrentGuard() {
        navigation.activate(Config.Ready(steamId = steamClient.currentSteamId.id))
    }

    private fun createInitialConfiguration(): Config {
        val instance = steamClient.ksteam.guard.instanceForCurrentUser()

        return if (instance != null) {
            Config.Ready(steamClient.currentSteamId.id)
        } else {
            Config.Setup
        }
    }

    private sealed interface Config : Parcelable {
        @Parcelize
        object Setup : Config

        @Parcelize
        class Ready(
            val steamId: ULong
        ) : Config
    }
}
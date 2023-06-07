package bruhcollective.itaysonlab.jetisteam

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.signin.DefaultSignRootComponent
import bruhcollective.itaysonlab.cobalt.signin.SignRootComponent
import bruhcollective.itaysonlab.jetisteam.navigation.CobaltContainerComponent
import bruhcollective.itaysonlab.ksteam.handlers.account
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.navigate
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.parcelize.Parcelize
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AndroidCobaltComponent (
    componentContext: ComponentContext,
): ComponentContext by componentContext, KoinComponent {
    private val steamClient: SteamClient by inject()

    private val slotNavigation = SlotNavigation<Config>()

    val slot: Value<ChildSlot<*, Slot>> = childSlot(
        source = slotNavigation,
        handleBackButton = false,
        childFactory = ::createSlot,
        initialConfiguration = ::createInitialConfiguration
    )

    private fun createSlot(config: Config, componentContext: ComponentContext): Slot {
        return when (config) {
            Config.SignIn -> Slot.SignIn(DefaultSignRootComponent(componentContext, onAuthorizationCompleted = this::onAuthorizationCompleted))
            Config.Cobalt -> Slot.Cobalt(CobaltContainerComponent(componentContext))
        }
    }

    private fun createInitialConfiguration(): Config {
        return if (steamClient.ksteam.account.hasSavedDataForAtLeastOneAccount()) {
            Config.Cobalt
        } else {
            Config.SignIn
        }
    }

    fun onAuthorizationCompleted() {
        slotNavigation.activate(Config.Cobalt)
    }

    sealed class Slot {
        class SignIn(val component: SignRootComponent) : Slot()
        class Cobalt(val component: CobaltContainerComponent) : Slot()
    }

    @Parcelize
    private sealed class Config : Parcelable {
        object SignIn : Config()
        object Cobalt : Config()
    }
}
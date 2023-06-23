package bruhcollective.itaysonlab.cobalt.guard.setup.onboarding

import bruhcollective.itaysonlab.cobalt.core.decompose.componentCoroutineScope
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.guard.setup.alert.DefaultGuardAlreadyExistsAlertComponent
import bruhcollective.itaysonlab.ksteam.handlers.guard
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class DefaultGuardOnboardingComponent (
    componentContext: ComponentContext
): GuardOnboardingComponent, ComponentContext by componentContext, KoinComponent {
    private val scope = componentCoroutineScope()
    private val steam by inject<SteamClient>()

    override val isTryingToStartSetup = MutableValue(false)

    override fun onSetupClicked() {
        scope.launch {
            isTryingToStartSetup.value = true
            steam.ksteam.guard.initializeSgCreation()
            isTryingToStartSetup.value = false
        }
    }
}
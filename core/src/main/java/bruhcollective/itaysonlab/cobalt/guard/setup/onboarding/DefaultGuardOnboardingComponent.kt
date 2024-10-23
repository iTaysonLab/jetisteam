package bruhcollective.itaysonlab.cobalt.guard.setup.onboarding

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.guard.setup.alert.DefaultGuardAlreadyExistsAlertComponent
import bruhcollective.itaysonlab.ksteam.guard.models.SgCreationResult
import bruhcollective.itaysonlab.ksteam.models.SteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class DefaultGuardOnboardingComponent (
    componentContext: ComponentContext,
    private val onSmsSent: (SteamId, SgCreationResult.AwaitingConfirmation) -> Unit
): GuardOnboardingComponent, ComponentContext by componentContext, CoroutineScope by componentContext.coroutineScope(), KoinComponent {
    private val steam by inject<SteamClient>()
    private val alertNavigation = SlotNavigation<AlertConfig>()

    override val isTryingToStartSetup = MutableValue(false)

    override val alertSlot: Value<ChildSlot<*, GuardOnboardingComponent.AlertChild>> = childSlot(
        source = alertNavigation,
        serializer = AlertConfig.serializer(),
        handleBackButton = true,
        childFactory = { config, componentContext ->
            when (config) {
                AlertConfig.OverrideExisting -> {
                    GuardOnboardingComponent.AlertChild.OverrideExisting(
                        DefaultGuardAlreadyExistsAlertComponent(componentContext, onConfirm = { msg ->
                            alertNavigation.dismiss()
                            onSmsSent(steam.currentSteamId, msg)
                        }, onCancel = alertNavigation::dismiss)
                    )
                }
            }
        }
    )

    override fun onSetupClicked() {
        launch {
            isTryingToStartSetup.value = true

            when (val result = steam.ksteam.guard.initializeSgCreation()) {
                SgCreationResult.AlreadyHasGuard -> {
                    alertNavigation.activate(AlertConfig.OverrideExisting)
                }

                is SgCreationResult.AwaitingConfirmation -> {
                    onSmsSent(steam.currentSteamId, result)
                }

                is SgCreationResult.Error -> {
                    // TODO
                }
            }

            isTryingToStartSetup.value = false
        }
    }

    @Serializable
    private sealed interface AlertConfig {
        @Serializable
        data object OverrideExisting: AlertConfig
    }
}
package bruhcollective.itaysonlab.cobalt.guard.setup

import bruhcollective.itaysonlab.cobalt.core.decompose.ViewModel
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.guard.setup.alert.DefaultGuardAlreadyExistsAlertComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.onboarding.DefaultGuardOnboardingComponent
import bruhcollective.itaysonlab.ksteam.guard.models.SgCreationFlowState
import bruhcollective.itaysonlab.ksteam.handlers.guard
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.observe
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.parcelize.Parcelize
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class DefaultGuardSetupComponent (
    componentContext: ComponentContext
): GuardSetupComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()
    private val alertNavigation = SlotNavigation<AlertConfig>()
    private val viewModel = instanceKeeper.getOrCreate { SetupViewModel() }

    override val childStack: Value<ChildStack<*, GuardSetupComponent.Child>> = childStack(
        source = navigation,
        initialConfiguration = Config.Onboarding,
        handleBackButton = true,
        childFactory = ::createChild
    )

    override val alert: Value<ChildSlot<*, GuardSetupComponent.AlertChild>> = childSlot(
        source = alertNavigation,
        handleBackButton = true,
        childFactory = ::createAlertSlot
    )

    override fun onAlertDismissed() {
        alertNavigation.dismiss()
    }

    init {
        viewModel.guardState.observe(lifecycle) { newState ->
            when (newState) {
                is SgCreationFlowState.AlreadyHasGuard -> {
                    alertNavigation.activate(AlertConfig.GuardAlreadyExists)
                }

                else -> {
                    println("Unknown state: $newState")
                }
            }
        }
    }

    private fun createChild(config: Config, componentContext: ComponentContext): GuardSetupComponent.Child {
        return when (config) {
            Config.Onboarding -> GuardSetupComponent.Child.Onboarding(DefaultGuardOnboardingComponent(componentContext))
        }
    }

    private fun createAlertSlot(config: AlertConfig, componentContext: ComponentContext): GuardSetupComponent.AlertChild {
        return when (config) {
            AlertConfig.GuardAlreadyExists -> GuardSetupComponent.AlertChild.GuardAlreadyExists(
                DefaultGuardAlreadyExistsAlertComponent(componentContext, onConfirm = {
                    // TODO
                }, onCancel = ::onAlertDismissed)
            )
        }
    }

    private sealed interface Config : Parcelable {
        @Parcelize
        object Onboarding : Config
    }

    private sealed interface AlertConfig : Parcelable {
        @Parcelize
        object GuardAlreadyExists : AlertConfig
    }

    private class SetupViewModel: ViewModel(), KoinComponent {
        private val steam by inject<SteamClient>()

        val guardState = MutableValue<SgCreationFlowState>(SgCreationFlowState.TryingToAdd)

        init {
            steam.ksteam.guard.guardConfigurationFlow.onEach {
                guardState.value = it
            }.launchIn(viewModelScope)
        }
    }
}
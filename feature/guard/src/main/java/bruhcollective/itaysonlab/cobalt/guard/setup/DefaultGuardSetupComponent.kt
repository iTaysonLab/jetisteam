package bruhcollective.itaysonlab.cobalt.guard.setup

import bruhcollective.itaysonlab.cobalt.core.decompose.ViewModel
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.guard.setup.alert.DefaultGuardAlreadyExistsAlertComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.onboarding.DefaultGuardOnboardingComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.recovery.GuardRecoveryCodeComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.recovery.SetupGuardRecoveryCodeComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.sms.DefaultGuardEnterSmsComponent
import bruhcollective.itaysonlab.cobalt.guard.setup.sms.GuardEnterSmsComponent
import bruhcollective.itaysonlab.ksteam.guard.models.SgCreationFlowState
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.ksteam.models.toSteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.bringToFront
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.observe
import com.arkivanov.essenty.instancekeeper.getOrCreate
import com.arkivanov.essenty.parcelable.Parcelable
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class DefaultGuardSetupComponent (
    private val onSuccess: () -> Unit,
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
        viewModel.resetGuard()
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
            Config.EnterSmsCode -> GuardSetupComponent.Child.EnterSmsCode(enterSmsCodeComponent(componentContext))
            is Config.ShowRecoveryCode -> GuardSetupComponent.Child.SaveRecoveryCode(saveRecoveryCodeComponent(config, componentContext))
        }
    }

    private fun enterSmsCodeComponent(componentContext: ComponentContext): GuardEnterSmsComponent {
        return DefaultGuardEnterSmsComponent(onExitClicked = {
            viewModel.resetGuard()
            navigation.bringToFront(Config.Onboarding)
        }, componentContext)
    }

    private fun saveRecoveryCodeComponent(
        config: Config.ShowRecoveryCode,
        componentContext: ComponentContext
    ): GuardRecoveryCodeComponent {
        return SetupGuardRecoveryCodeComponent(
            userId = config.steamId.toSteamId(),
            code = config.code,
            onExitClicked = onSuccess,
            componentContext = componentContext
        )
    }

    private fun createAlertSlot(config: AlertConfig, componentContext: ComponentContext): GuardSetupComponent.AlertChild {
        return when (config) {
            AlertConfig.GuardAlreadyExists -> GuardSetupComponent.AlertChild.GuardAlreadyExists(
                DefaultGuardAlreadyExistsAlertComponent(componentContext, onConfirm = viewModel::confirmMove, onCancel = ::onAlertDismissed)
            )
        }
    }

    private sealed interface Config : Parcelable {
        @Parcelize
        object Onboarding : Config

        @Parcelize
        object EnterSmsCode : Config

        @Parcelize
        class ShowRecoveryCode(
            val steamId: ULong,
            val code: String
        ) : Config
    }

    private sealed interface AlertConfig : Parcelable {
        @Parcelize
        object GuardAlreadyExists : AlertConfig
    }

    private class SetupViewModel: ViewModel(), KoinComponent {
        private val steam by inject<SteamClient>()

        val guardState = MutableValue<SgCreationFlowState>(SgCreationFlowState.Idle)

        init {
            steam.ksteam.guard.guardConfigurationFlow.onEach {
                guardState.value = it
            }.launchIn(viewModelScope)
        }

        fun confirmMove() {
            viewModelScope.launch {
                steam.ksteam.guard.confirmMove()
            }
        }

        fun resetGuard() {
            steam.ksteam.guard.resetSgCreation()
        }
    }
}
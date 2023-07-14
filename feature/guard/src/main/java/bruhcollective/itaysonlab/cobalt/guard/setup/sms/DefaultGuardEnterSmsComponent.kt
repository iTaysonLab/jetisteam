package bruhcollective.itaysonlab.cobalt.guard.setup.sms

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamViewModel
import bruhcollective.itaysonlab.ksteam.handlers.guard
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.instancekeeper.getOrCreate
import kotlinx.coroutines.launch

internal class DefaultGuardEnterSmsComponent (
    private val onExitClicked: () -> Unit,
    componentContext: ComponentContext
): GuardEnterSmsComponent, ComponentContext by componentContext {
    private val viewModel = instanceKeeper.getOrCreate {
        SmsViewModel()
    }

    override val code get() = viewModel.code
    override val state get() = viewModel.state
    override fun onExitClicked() = onExitClicked.invoke()
    override fun onSubmitCodeClicked() = viewModel.submitCode()
    override fun onCodeChanged(value: String) = viewModel.onCodeChanged(value)

    private class SmsViewModel: SteamViewModel() {
        val code = MutableValue("")
        val state = MutableValue(GuardEnterSmsComponent.State.AwaitingInput)

        fun onCodeChanged(newValue: String) {
            code.value = newValue

            state.update {
                if (newValue.length == 5) {
                    GuardEnterSmsComponent.State.ValidCodeEntered
                } else {
                    GuardEnterSmsComponent.State.AwaitingInput
                }
            }
        }

        fun submitCode() {
            if (state.value != GuardEnterSmsComponent.State.ValidCodeEntered) {
                return
            }

            state.value = GuardEnterSmsComponent.State.Submitting

            viewModelScope.launch {
                if (steam.ksteam.guard.confirmSgConfiguration(code.value).not()) {
                    state.value = GuardEnterSmsComponent.State.InvalidInformation
                }
            }
        }
    }
}
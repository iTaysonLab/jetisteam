package bruhcollective.itaysonlab.cobalt.guard.setup.recovery

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamViewModel
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.ksteam.handlers.persona
import bruhcollective.itaysonlab.ksteam.models.SteamId
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.instancekeeper.getOrCreate
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

internal class SetupGuardRecoveryCodeComponent (
    private val userId: SteamId,
    override val code: String,
    private val onExitClicked: () -> Unit,
    componentContext: ComponentContext
): GuardRecoveryCodeComponent, ComponentContext by componentContext {
    private val viewModel = instanceKeeper.getOrCreate {
        RecoveryViewModel(userId)
    }

    override val user get() = viewModel.user

    override fun onExitClicked() = onExitClicked.invoke()

    private class RecoveryViewModel(
        private val id: SteamId
    ): SteamViewModel() {
        val user = MutableValue(Persona.Unknown)

        init {
            viewModelScope.launch {
                steam.ksteam.persona.persona(id).onEach { persona ->
                    user.value = persona
                }.collect()
            }
        }
    }
}
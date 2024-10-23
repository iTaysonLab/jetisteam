package bruhcollective.itaysonlab.cobalt.guard.instance.confirmations

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.guard.instance.code.GuardCodeComponent
import bruhcollective.itaysonlab.ksteam.guard.models.ConfirmationListState
import bruhcollective.itaysonlab.ksteam.guard.models.MobileConfirmationItem
import bruhcollective.itaysonlab.ksteam.models.SteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnResume
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DefaultGuardConfirmationsComponent(
    private val steamId: SteamId,
    private val onConfirmationClicked: (MobileConfirmationItem) -> Unit,
    componentContext: ComponentContext
): GuardConfirmationsComponent, ComponentContext by componentContext, KoinComponent {
    private val scope = coroutineScope()
    private val steamClient by inject<SteamClient>()

    override val state = MutableValue<ConfirmationListState>(ConfirmationListState.Loading)
    override val isRefreshing = MutableValue(false)

    override fun onRefresh() {
        scope.launch {
            if (isRefreshing.value) return@launch

            isRefreshing.update { true }
            loadConfirmations()
            isRefreshing.update { false }
        }
    }

    override fun onConfirmationClicked(confirmation: MobileConfirmationItem) {
        onConfirmationClicked.invoke(confirmation)
    }

    private suspend fun loadConfirmations() {
        val data = withContext(Dispatchers.IO) {
            steamClient.ksteam.guardConfirmation.getConfirmations(steamId)
        }

        state.update { data }
    }

    init {
        lifecycle.doOnResume {
            onRefresh()
        }
    }
}
package bruhcollective.itaysonlab.cobalt.guard.instance.code

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.models.SteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.Lifecycle
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.coroutines.withLifecycle
import com.arkivanov.essenty.lifecycle.doOnCreate
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DefaultGuardCodeComponent (
    private val steamId: SteamId,
    private val onRecoveryButtonClicked: () -> Unit,
    private val onDeleteButtonClicked: () -> Unit,
    componentContext: ComponentContext
): GuardCodeComponent, ComponentContext by componentContext, KoinComponent {
    private val scope = coroutineScope()
    private val steamClient by inject<SteamClient>()

    override val code = MutableValue("")
    override val codeProgress = MutableValue(0f)

    override fun onRecoveryCodeButtonClicked() {
        onRecoveryButtonClicked.invoke()
    }

    override fun onDeleteGuardButtonClicked() {
        onDeleteButtonClicked.invoke()
    }

    override fun onCopyButtonClicked() {
        // TODO: PlatformClipboard?
    }

    init {
        scope.launch {
            steamClient.ksteam.guard.instanceFor(steamId)
                ?.code
                ?.withLifecycle(lifecycle, minActiveState = Lifecycle.State.RESUMED)
                ?.collectLatest { model ->
                    code.update { model.code }
                    codeProgress.update { model.progressRemaining }
                }
        }
    }
}
package bruhcollective.itaysonlab.cobalt.guard.setup.sms

import bruhcollective.itaysonlab.cobalt.core.decompose.componentCoroutineScope
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.handlers.guard
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class DefaultGuardEnterSmsComponent (
    override val isMovingGuard: Boolean,
    override val phoneNumberHint: String,
    private val onExitClicked: () -> Unit,
    componentContext: ComponentContext
): GuardEnterSmsComponent, ComponentContext by componentContext, KoinComponent {
    private val steamClient: SteamClient by inject()
    private val componentScope = componentCoroutineScope()

    override val codeRow = DefaultCodeRowComponent(codeLength = 5, onEntryFinish = { code ->
        onSubmitCodeClicked(code)
    })

    override fun onExitClicked() = onExitClicked.invoke()

    override fun onSubmitCodeClicked(code: String) {
        componentScope.launch {
            codeRow.setInactive(true)
            codeRow.setError(!steamClient.ksteam.guard.confirmSgConfiguration(code))
            codeRow.setInactive(false)
        }
    }
}
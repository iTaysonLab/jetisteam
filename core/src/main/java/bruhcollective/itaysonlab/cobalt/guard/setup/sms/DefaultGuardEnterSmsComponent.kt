package bruhcollective.itaysonlab.cobalt.guard.setup.sms

import bruhcollective.itaysonlab.cobalt.core.decompose.componentCoroutineScope
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.guard.models.GuardStructure
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.ksteam.models.SteamId
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class DefaultGuardEnterSmsComponent (
    override val isMovingGuard: Boolean,
    override val phoneNumberHint: String,

    private val guardStructure: GuardStructure?,

    private val onExitClicked: () -> Unit,
    private val onGuardAdded: (String) -> Unit,

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

            val revocationCode = if (isMovingGuard) {
                steamClient.ksteam.guard.confirmSgMoving(code = code)
            } else {
                steamClient.ksteam.guard.confirmSgCreation(code = code, structure = guardStructure!!)
            }?.revocationCode

            if (revocationCode != null) {
                onGuardAdded(revocationCode)
            } else {
                codeRow.setInactive(false)
                codeRow.setError(true)
            }
        }
    }
}
package bruhcollective.itaysonlab.cobalt.signin.tfa

import bruhcollective.itaysonlab.cobalt.core.decompose.componentCoroutineScope
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.models.account.AuthorizationState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class DefaultTwoFactorComponent (
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
    private val onAuthorizationCompleted: () -> Unit
): TwoFactorComponent, KoinComponent, ComponentContext by componentContext {
    private val steamClient: SteamClient by inject()
    private val scope = componentCoroutineScope()

    override val code = MutableValue("")

    override fun onCodeChanged(value: String) {
        code.value = value
    }

    override fun submitCode() {

    }

    override fun onBackClicked() {
        onBack.invoke()
    }

    init {
        steamClient.ksteam.account.clientAuthState.onEach { newState ->
            when (newState) {
                AuthorizationState.Unauthorized -> error("Unauthorized authState but 2FA still called")
                is AuthorizationState.AwaitingTwoFactor -> { println(newState.steamId) }
                AuthorizationState.Success -> onAuthorizationCompleted()
            }
        }.launchIn(scope)
    }
}
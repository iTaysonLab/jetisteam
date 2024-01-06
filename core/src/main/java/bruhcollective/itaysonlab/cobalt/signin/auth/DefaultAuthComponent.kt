package bruhcollective.itaysonlab.cobalt.signin.auth

import bruhcollective.itaysonlab.cobalt.core.decompose.componentCoroutineScope
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.handlers.Account
import bruhcollective.itaysonlab.ksteam.handlers.account
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DefaultAuthComponent (
    componentContext: ComponentContext,
    private val onProceedToTfa: () -> Unit
): AuthComponent, ComponentContext by componentContext, KoinComponent {
    private val scope = componentCoroutineScope()

    private val steamClient: SteamClient by inject()

    override val username = MutableValue("")
    override val password = MutableValue("")
    override val signInState = MutableValue(AuthComponent.SignInState.NeedInformation)

    override fun trySignIn() {
        scope.launch {
            signInState.value = AuthComponent.SignInState.Processing

            val signTry = steamClient.ksteam.account.signIn(
                username = username.value,
                password = password.value
            )

            when (signTry) {
                Account.AuthorizationResult.InvalidPassword -> {
                    signInState.value = AuthComponent.SignInState.InvalidInformation
                }

                Account.AuthorizationResult.RpcError -> {
                    signInState.value = AuthComponent.SignInState.InvalidInformation
                }

                Account.AuthorizationResult.Success -> {
                    onProceedToTfa()
                }
            }
        }
    }

    override fun onUsernameChanged(value: String) {
        username.value = value
        verifyForm()
    }

    override fun onPasswordChanged(value: String) {
        password.value = value
        verifyForm()
    }

    private fun verifyForm() {
        signInState.value = if (username.value.isNotEmpty() && password.value.isNotEmpty()) {
            AuthComponent.SignInState.CanSignIn
        } else {
            AuthComponent.SignInState.NeedInformation
        }
    }
}
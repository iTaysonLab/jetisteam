package bruhcollective.itaysonlab.cobalt.signin.auth

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.handlers.Account
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class DefaultAuthComponent (
    componentContext: ComponentContext,
    private val onProceedToTfa: () -> Unit
): AuthComponent, ComponentContext by componentContext, KoinComponent, CoroutineScope by componentContext.coroutineScope() {

    private val steamClient: SteamClient by inject()

    override val username = MutableValue("")
    override val password = MutableValue("")
    override val signInState = MutableValue(AuthComponent.SignInState.NeedInformation)

    override fun trySignIn() {
        launch {
            signInState.value = AuthComponent.SignInState.Processing

            val signTry = kotlin.runCatching {
                steamClient.ksteam.account.signIn(
                    username = username.value,
                    password = password.value
                )
            }.getOrElse {
                it.printStackTrace()
                Account.AuthorizationResult.RpcError
            }

            when (signTry) {
                Account.AuthorizationResult.InvalidPassword -> {
                    signInState.value = AuthComponent.SignInState.InvalidInformation
                }

                Account.AuthorizationResult.RpcError -> {
                    signInState.value = AuthComponent.SignInState.RpcError
                }

                Account.AuthorizationResult.ProceedToTfa -> {
                    signInState.value = AuthComponent.SignInState.CanSignIn
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
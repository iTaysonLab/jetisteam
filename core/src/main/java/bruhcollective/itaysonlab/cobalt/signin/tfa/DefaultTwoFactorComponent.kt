package bruhcollective.itaysonlab.cobalt.signin.tfa

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.models.account.AuthorizationState
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class DefaultTwoFactorComponent (
    componentContext: ComponentContext,
    private val onBack: () -> Unit,
    private val onAuthorizationCompleted: () -> Unit
): TwoFactorComponent, KoinComponent, ComponentContext by componentContext, CoroutineScope by componentContext.coroutineScope() {
    private val steamClient: SteamClient by inject()

    override val code = MutableValue("")
    override val state = MutableValue(TwoFactorComponent.State.AwaitingCode)
    override val codeSource = MutableValue<TwoFactorComponent.CodeSource>(TwoFactorComponent.CodeSource.SteamGuard(true))

    override fun onCodeChanged(value: String) {
        code.value = value

        state.value =  if (value.isNotEmpty()) {
            TwoFactorComponent.State.CanSubmitCode
        } else {
            TwoFactorComponent.State.AwaitingCode
        }
    }

    override fun submitCode() {
        launch {
            state.value = TwoFactorComponent.State.SubmittingCode

            kotlin.runCatching {
                if (steamClient.ksteam.account.updateCurrentSessionWithCode(code.value).not()) {
                    state.value = TwoFactorComponent.State.WrongCode
                }
            }.onFailure {
                it.printStackTrace()
                state.value = TwoFactorComponent.State.RpcError
            }
        }
    }

    override fun onBackClicked() {
        steamClient.ksteam.account.cancelPolling()
        onBack.invoke()
    }

    init {
        launch {
            steamClient.ksteam.account.clientAuthState.collect { newState ->
                when (newState) {
                    AuthorizationState.Unauthorized -> {}

                    AuthorizationState.Success -> {
                        onAuthorizationCompleted()
                    }

                    is AuthorizationState.AwaitingTwoFactor -> {
                        val containsEmail = newState.supportedConfirmationMethods.contains(AuthorizationState.AwaitingTwoFactor.ConfirmationMethod.EmailCode)
                        val containsEmailConfirmation = newState.supportedConfirmationMethods.contains(AuthorizationState.AwaitingTwoFactor.ConfirmationMethod.EmailConfirmation)
                        val containsDevice = newState.supportedConfirmationMethods.contains(AuthorizationState.AwaitingTwoFactor.ConfirmationMethod.DeviceCode)
                        val containsDeviceConfirmation = newState.supportedConfirmationMethods.contains(AuthorizationState.AwaitingTwoFactor.ConfirmationMethod.DeviceConfirmation)

                        codeSource.value = when {
                            containsEmail -> TwoFactorComponent.CodeSource.EmailCode
                            containsEmailConfirmation -> TwoFactorComponent.CodeSource.EmailConfirmation
                            containsDevice || containsDeviceConfirmation -> TwoFactorComponent.CodeSource.SteamGuard(containsDeviceConfirmation)
                            else -> TwoFactorComponent.CodeSource.SteamGuard(false)
                        }
                    }
                }
            }
        }
    }
}
package bruhcollective.itaysonlab.cobalt.signin.tfa

import com.arkivanov.decompose.value.Value

interface TwoFactorComponent {
    val codeSource: Value<CodeSource>
    val code: Value<String>
    val state: Value<State>

    fun onCodeChanged(value: String)
    fun submitCode()

    fun onBackClicked()

    enum class State {
        AwaitingCode, CanSubmitCode, SubmittingCode, WrongCode, RpcError
    }

    sealed interface CodeSource {
        data class SteamGuard (
            val canAutomaticallyConfirm: Boolean
        ): CodeSource

        data object EmailCode: CodeSource
        data object EmailConfirmation: CodeSource
    }
}
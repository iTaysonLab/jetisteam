package bruhcollective.itaysonlab.cobalt.signin.auth

import com.arkivanov.decompose.value.Value

interface AuthComponent {
    val username: Value<String>
    val password: Value<String>

    val signInState: Value<SignInState>

    fun trySignIn()

    fun onUsernameChanged(value: String)
    fun onPasswordChanged(value: String)

    enum class SignInState {
        NeedInformation, CanSignIn, Processing, InvalidInformation, RpcError
    }
}
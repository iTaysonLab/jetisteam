package bruhcollective.itaysonlab.cobalt.signin.tfa

import com.arkivanov.decompose.value.Value

interface TwoFactorComponent {
    val code: Value<String>
    fun onCodeChanged(value: String)

    fun submitCode()

    fun onBackClicked()
}
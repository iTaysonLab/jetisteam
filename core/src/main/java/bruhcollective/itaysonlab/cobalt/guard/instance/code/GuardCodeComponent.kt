package bruhcollective.itaysonlab.cobalt.guard.instance.code

import com.arkivanov.decompose.value.Value

interface GuardCodeComponent {
    val code: Value<String>
    val codeProgress: Value<Float>

    fun onRecoveryCodeButtonClicked()
    fun onDeleteGuardButtonClicked()
    fun onCopyButtonClicked()
}
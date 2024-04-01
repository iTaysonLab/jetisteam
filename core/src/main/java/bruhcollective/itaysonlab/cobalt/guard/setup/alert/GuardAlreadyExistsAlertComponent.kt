package bruhcollective.itaysonlab.cobalt.guard.setup.alert

import com.arkivanov.decompose.value.Value

interface GuardAlreadyExistsAlertComponent {
    val isConfirmingReplacement: Value<Boolean>

    fun confirm()
    fun dismiss()
}
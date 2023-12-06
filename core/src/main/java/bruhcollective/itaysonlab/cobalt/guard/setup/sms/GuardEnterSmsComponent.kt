package bruhcollective.itaysonlab.cobalt.guard.setup.sms

import com.arkivanov.decompose.value.Value

interface GuardEnterSmsComponent {
    val code: Value<String>
    val state: Value<State>

    fun onExitClicked()
    fun onSubmitCodeClicked()
    fun onCodeChanged(value: String)

    enum class State {
        AwaitingInput, ValidCodeEntered, Submitting, InvalidInformation
    }
}
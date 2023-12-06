package bruhcollective.itaysonlab.cobalt.guard.setup.recovery

import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import com.arkivanov.decompose.value.Value

interface GuardRecoveryCodeComponent {
    val user: Value<Persona>
    val code: String

    fun onExitClicked()
}
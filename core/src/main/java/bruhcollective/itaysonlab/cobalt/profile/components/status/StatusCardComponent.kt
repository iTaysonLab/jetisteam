package bruhcollective.itaysonlab.cobalt.profile.components.status

import bruhcollective.itaysonlab.ksteam.models.apps.AppSummary
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import com.arkivanov.decompose.value.Value

interface StatusCardComponent {
    val status: Value<Persona.Status>
    val appInformation: Value<AppSummary>

    fun onPersonaUpdated(persona: Persona)
}
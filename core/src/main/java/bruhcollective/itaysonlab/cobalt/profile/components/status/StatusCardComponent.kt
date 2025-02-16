package bruhcollective.itaysonlab.cobalt.profile.components.status

import bruhcollective.itaysonlab.cobalt.core.ksteam.NullableSteamApplication
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import com.arkivanov.decompose.value.Value

interface StatusCardComponent {
    val status: Value<Persona.Status>
    val appInformation: Value<NullableSteamApplication>

    fun onPersonaUpdated(persona: Persona)
}
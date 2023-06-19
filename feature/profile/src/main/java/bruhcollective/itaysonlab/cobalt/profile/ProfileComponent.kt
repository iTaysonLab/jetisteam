package bruhcollective.itaysonlab.cobalt.profile

import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileEquipment
import kotlinx.coroutines.flow.StateFlow

interface ProfileComponent {
    val persona: StateFlow<Persona>
    val personaEquipment: StateFlow<ProfileEquipment>

    fun dispatchComponentLoad()
}
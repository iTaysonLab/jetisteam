package bruhcollective.itaysonlab.cobalt.profile.components.header

import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileEquipment
import com.arkivanov.decompose.value.Value

interface ProfileHeaderComponent {
    val title: Value<String>

    val staticAvatarUrl: Value<String>
    val animatedAvatarUrl: Value<String>

    val staticBackgroundUrl: Value<String>
    val animatedBackgroundUrl: Value<String>

    fun onPersonaUpdated(persona: Persona)
    fun onPersonaEquipmentUpdated(equipment: ProfileEquipment)
}
package bruhcollective.itaysonlab.cobalt.profile.components.header

import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileEquipment
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue

internal class DefaultProfileHeaderComponent (
    componentContext: ComponentContext
): ProfileHeaderComponent, ComponentContext by componentContext {
    override val title = MutableValue("")

    override val staticAvatarUrl = MutableValue("")
    override val animatedAvatarUrl = MutableValue("")
    override val staticBackgroundUrl = MutableValue("")
    override val animatedBackgroundUrl = MutableValue("")

    override fun onPersonaUpdated(persona: Persona) {
        title.value = persona.name
        staticAvatarUrl.value = persona.avatar.full
    }

    override fun onPersonaEquipmentUpdated(equipment: ProfileEquipment) {
        animatedAvatarUrl.value = equipment.animatedAvatar?.movieMp4.orEmpty()
        staticBackgroundUrl.value = equipment.background?.imageLarge.orEmpty()
        animatedBackgroundUrl.value = equipment.background?.movieMp4Small.orEmpty()
    }
}
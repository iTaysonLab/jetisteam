package bruhcollective.itaysonlab.cobalt.profile.components.widgets

import bruhcollective.itaysonlab.ksteam.models.persona.ProfileCustomization
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileWidget
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList

interface ProfileWidgetsComponent {
    val widgets: Value<ImmutableList<ProfileWidget>>

    fun onPersonaCustomizationChanged(customization: ProfileCustomization)
}
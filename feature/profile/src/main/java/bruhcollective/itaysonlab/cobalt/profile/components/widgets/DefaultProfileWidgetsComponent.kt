package bruhcollective.itaysonlab.cobalt.profile.components.widgets

import bruhcollective.itaysonlab.ksteam.models.persona.ProfileCustomization
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileWidget
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList

internal class DefaultProfileWidgetsComponent (
    componentContext: ComponentContext
): ProfileWidgetsComponent, ComponentContext by componentContext {
    override val widgets = MutableValue<ImmutableList<ProfileWidget>>(persistentListOf())

    override fun onPersonaCustomizationChanged(customization: ProfileCustomization) {
        widgets.value = customization.profileWidgets.toImmutableList()
    }
}
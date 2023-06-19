package bruhcollective.itaysonlab.cobalt.profile.components.header

import bruhcollective.itaysonlab.cobalt.profile.ProfileComponent
import com.arkivanov.decompose.ComponentContext

class DefaultProfileHeaderComponent (
    componentContext: ComponentContext
): ProfileHeaderComponent, ComponentContext by componentContext {

}
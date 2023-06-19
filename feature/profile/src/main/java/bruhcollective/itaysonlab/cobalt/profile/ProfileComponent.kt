package bruhcollective.itaysonlab.cobalt.profile

import bruhcollective.itaysonlab.cobalt.profile.components.header.ProfileHeaderComponent
import bruhcollective.itaysonlab.cobalt.profile.components.status.StatusCardComponent
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileWidget
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList

interface ProfileComponent {
    val state: Value<State>

    val headerComponent: ProfileHeaderComponent
    val statusCardComponent: StatusCardComponent
    val widgets: Value<ImmutableList<ProfileWidget>>

    fun dispatchComponentLoad()

    sealed interface State {
        object Loading: State
        object Ready: State
    }
}
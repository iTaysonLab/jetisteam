package bruhcollective.itaysonlab.cobalt.profile

import bruhcollective.itaysonlab.cobalt.profile.components.header.ProfileHeaderComponent
import bruhcollective.itaysonlab.cobalt.profile.components.status.StatusCardComponent
import bruhcollective.itaysonlab.cobalt.profile.components.widgets.ProfileWidgetsComponent
import com.arkivanov.decompose.value.Value

interface ProfileComponent {
    val state: Value<State>

    val headerComponent: ProfileHeaderComponent
    val statusCardComponent: StatusCardComponent
    val widgetsComponent: ProfileWidgetsComponent

    fun dispatchComponentLoad()

    sealed interface State {
        object Idle: State
        object Loading: State
        object Ready: State
    }
}
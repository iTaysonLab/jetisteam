package bruhcollective.itaysonlab.cobalt.guard.bottom_sheet

import bruhcollective.itaysonlab.ksteam.guard.models.IncomingSession
import com.arkivanov.decompose.value.Value

interface GuardIncomingSessionComponent {
    val username: String

    val state: Value<State>
    val shouldRememberPassword: Value<Boolean>

    val isConfirmationInProgress: Value<Boolean>
    val isCancellationInProgress: Value<Boolean>

    fun setShouldRememberPassword(value: Boolean)
    fun confirmSession()
    fun cancelSession()

    sealed interface State {
        data object Loading: State

        data class Ready (
            val session: IncomingSession
        ): State
    }
}
package bruhcollective.itaysonlab.cobalt.guard.instance.sessions

import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

interface GuardSessionsComponent {
    val sessions: Value<ImmutableList<ActiveSession>>
    val isRefreshing: Value<Boolean>
    val isLoading: Value<Boolean>
    val currentSession: Value<ActiveSession>

    fun onRefresh()
    fun onSessionClicked(session: ActiveSession)
}
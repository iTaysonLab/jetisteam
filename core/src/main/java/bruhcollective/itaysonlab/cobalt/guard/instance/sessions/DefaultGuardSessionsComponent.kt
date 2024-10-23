package bruhcollective.itaysonlab.cobalt.guard.instance.sessions

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.ksteam.models.SteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.update
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnResume
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import steam.webui.authentication.CAuthentication_RefreshToken_Enumerate_Response_RefreshTokenDescription

class DefaultGuardSessionsComponent(
    private val steamId: SteamId,
    private val onSessionClicked: (ActiveSession) -> Unit,
    componentContext: ComponentContext
): GuardSessionsComponent, ComponentContext by componentContext, KoinComponent {
    private val scope = coroutineScope()
    private val steamClient by inject<SteamClient>()

    override val sessions = MutableValue<ImmutableList<ActiveSession>>(persistentListOf())
    override val currentSession = MutableValue<ActiveSession>(ActiveSession(CAuthentication_RefreshToken_Enumerate_Response_RefreshTokenDescription()))
    override val isRefreshing = MutableValue(false)
    override val isLoading = MutableValue(false)

    override fun onRefresh() {
        scope.launch {
            if (isRefreshing.value) return@launch

            isRefreshing.update { true }
            loadSessions()
            isRefreshing.update { false }
        }
    }

    override fun onSessionClicked(session: ActiveSession) {
        onSessionClicked.invoke(session)
    }

    private suspend fun loadSessions() {
        val data = withContext(Dispatchers.IO) { steamClient.ksteam.guardManagement.getActiveSessions() }
        sessions.update { data.sessions.toPersistentList() }
        currentSession.update { data.currentSession }
    }

    init {
        scope.launch {
            isLoading.update { true }
            loadSessions()
            isLoading.update { false }
        }
    }
}
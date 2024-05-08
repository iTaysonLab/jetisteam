package bruhcollective.itaysonlab.cobalt.guard.instance

import bruhcollective.itaysonlab.ksteam.ExtendedSteamClient
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.ksteam.guard.models.CodeModel
import bruhcollective.itaysonlab.ksteam.guard.models.ConfirmationListState
import bruhcollective.itaysonlab.ksteam.models.SteamId
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalMviKotlinApi::class)
internal fun GuardInstanceStore(
    storeFactory: StoreFactory,
    steamClient: ExtendedSteamClient,
    initialState: GuardInstanceState,
    codeFlow: Flow<CodeModel>,
    mainContext: CoroutineContext,
): Store<GuardInstanceIntent, GuardInstanceState, Nothing> = storeFactory.create<GuardInstanceIntent, GuardInstanceAction, GuardInstanceMsg, GuardInstanceState, Nothing>(
    name = "GuardInstanceStore",
    initialState = initialState,
    bootstrapper = coroutineBootstrapper(mainContext) {
        steamClient.guard.instanceFor(initialState.steamId)?.revocationCode?.let { revocationCode ->
            dispatch(GuardInstanceAction.RevocationCodeUpdated(revocationCode))
        }

        // Run code collecting in BG
        codeFlow.map(::modelToAction)
            .onEach(this::dispatch)
            .launchIn(this)
    },
    executorFactory = coroutineExecutorFactory(mainContext) {
        onAction<GuardInstanceAction.CodeUpdated> { action ->
            dispatch(GuardInstanceMsg.CodeUpdated(code = action.code, progress = action.progress))
        }

        onAction<GuardInstanceAction.RevocationCodeUpdated> { action ->
            dispatch(GuardInstanceMsg.RevocationCodeUpdated(code = action.code))
        }

        onIntent<GuardInstanceIntent.LoadActiveSessions> {
            launch {
                dispatch(GuardInstanceMsg.StartedSessionsLoad)

                steamClient.guardManagement.getActiveSessions().let { result ->
                    dispatch(GuardInstanceMsg.ActiveSessionUpdated(result.currentSession))
                    dispatch(GuardInstanceMsg.SessionsUpdated(result.sessions))
                }
            }
        }

        onIntent<GuardInstanceIntent.LoadMobileConfirmations> {
            launch {
                dispatch(GuardInstanceMsg.StartedConfirmationsLoad)
                dispatch(GuardInstanceMsg.ConfirmationsUpdated(steamClient.guardConfirmation.getConfirmations(steamId = state().steamId)))
            }
        }

        onIntent<GuardInstanceIntent.NotifySessionRevoked> { intent ->
            launch {
                dispatch(GuardInstanceMsg.SessionsUpdated(sessions = withContext(Dispatchers.Default) {
                    state().sessions.filterNot { it.id == intent.id }
                }))
            }
        }
    },
    reducer = { msg ->
        when (msg) {
            is GuardInstanceMsg.CodeUpdated -> copy(code = msg.code, codeProgress = msg.progress)
            is GuardInstanceMsg.ConfirmationsUpdated -> copy(confirmations = msg.listState, areConfirmationsLoading = false)
            is GuardInstanceMsg.SessionsUpdated -> copy(sessions = msg.sessions.toPersistentList(), areSessionsLoading = false)
            is GuardInstanceMsg.ActiveSessionUpdated -> copy(currentSession = msg.session)
            is GuardInstanceMsg.RevocationCodeUpdated -> copy(revocationCode = msg.code)
            GuardInstanceMsg.StartedConfirmationsLoad -> copy(areConfirmationsLoading = true)
            GuardInstanceMsg.StartedSessionsLoad -> copy(areSessionsLoading = true)
        }
    }
)

sealed class GuardInstanceIntent {
    data object LoadMobileConfirmations: GuardInstanceIntent()
    data object LoadActiveSessions: GuardInstanceIntent()
    data class NotifySessionRevoked ( val id: Long ): GuardInstanceIntent()
}

data class GuardInstanceState (
    val steamId: SteamId,
    val username: String,
    //
    val code: String = "",
    val codeProgress: Float = 0f,
    val revocationCode: String = "",
    // Confirmations
    val areConfirmationsLoading: Boolean = true,
    val confirmations: ConfirmationListState = ConfirmationListState.Loading,
    // Sessions
    val areSessionsLoading: Boolean = true,
    val currentSession: ActiveSession? = null,
    val sessions: ImmutableList<ActiveSession> = persistentListOf()
)

private sealed class GuardInstanceAction {
    data class RevocationCodeUpdated(val code: String): GuardInstanceAction()

    data class CodeUpdated(val code: String, val progress: Float): GuardInstanceAction() {
        constructor(model: CodeModel): this(code = model.code, progress = model.progressRemaining)
    }
}

private sealed class GuardInstanceMsg {
    data class CodeUpdated(val code: String, val progress: Float): GuardInstanceMsg() {
        constructor(model: CodeModel): this(code = model.code, progress = model.progressRemaining)
    }

    data class RevocationCodeUpdated(val code: String): GuardInstanceMsg()

    data object StartedConfirmationsLoad: GuardInstanceMsg()
    data object StartedSessionsLoad: GuardInstanceMsg()

    data class ConfirmationsUpdated(
        val listState: ConfirmationListState
    ): GuardInstanceMsg()

    data class ActiveSessionUpdated(
        val session: ActiveSession
    ): GuardInstanceMsg()

    data class SessionsUpdated(
        val sessions: List<ActiveSession>
    ): GuardInstanceMsg()
}

private fun modelToAction(model: CodeModel) = GuardInstanceAction.CodeUpdated(model)
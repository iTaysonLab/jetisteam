package bruhcollective.itaysonlab.cobalt.guard.instance

import bruhcollective.itaysonlab.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.ksteam.guard.models.CodeModel
import bruhcollective.itaysonlab.ksteam.guard.models.ConfirmationListState
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.ksteam.handlers.guardConfirmation
import bruhcollective.itaysonlab.ksteam.handlers.guardManagement
import bruhcollective.itaysonlab.ksteam.models.SteamId
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.core.utils.ExperimentalMviKotlinApi
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.coroutineExecutorFactory
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import steam.webui.authentication.CAuthentication_RefreshToken_Enumerate_Response_RefreshTokenDescription
import kotlin.coroutines.CoroutineContext

@OptIn(ExperimentalMviKotlinApi::class)
internal fun GuardInstanceStore(
    storeFactory: StoreFactory,
    steamClient: SteamClient,
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
                dispatch(GuardInstanceMsg.SessionsUpdated(steamClient.guardManagement.getActiveSessions()))
            }
        }

        onIntent<GuardInstanceIntent.LoadMobileConfirmations> {
            launch {
                dispatch(GuardInstanceMsg.ConfirmationsUpdated(steamClient.guardConfirmation.getConfirmations(steamId = state().steamId)))
            }
        }
    },
    reducer = { msg ->
        when (msg) {
            is GuardInstanceMsg.CodeUpdated -> copy(code = msg.code, codeProgress = msg.progress)
            is GuardInstanceMsg.ConfirmationsUpdated -> copy(confirmations = msg.listState)
            is GuardInstanceMsg.SessionsUpdated -> copy(sessions = msg.sessions.toPersistentList(), sessionsLoaded = true)
            is GuardInstanceMsg.RevocationCodeUpdated -> copy(revocationCode = msg.code)
        }
    }
)

sealed class GuardInstanceIntent {
    data object LoadMobileConfirmations: GuardInstanceIntent()
    data object LoadActiveSessions: GuardInstanceIntent()
}

data class GuardInstanceState (
    val steamId: SteamId,
    val username: String,
    //
    val code: String = "",
    val codeProgress: Float = 0f,
    val revocationCode: String = "",
    // Confirmations
    val confirmations: ConfirmationListState = ConfirmationListState.Loading,
    // Sessions
    val sessions: ImmutableList<ActiveSession> = persistentListOf(),
    val sessionsLoaded: Boolean = false,
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

    data class ConfirmationsUpdated(
        val listState: ConfirmationListState
    ): GuardInstanceMsg()

    data class SessionsUpdated(
        val sessions: List<ActiveSession>
    ): GuardInstanceMsg()
}

private fun modelToAction(model: CodeModel) = GuardInstanceAction.CodeUpdated(model)

// region Test store

@OptIn(ExperimentalMviKotlinApi::class)
internal fun TestGuardInstanceStore(
    storeFactory: StoreFactory,
    steamClient: SteamClient,
    initialState: GuardInstanceState,
    codeFlow: Flow<CodeModel>,
    mainContext: CoroutineContext,
): Store<GuardInstanceIntent, GuardInstanceState, Nothing> = storeFactory.create<GuardInstanceIntent, GuardInstanceAction, GuardInstanceMsg, GuardInstanceState, Nothing>(
    name = "GuardInstanceStore",
    initialState = initialState,
    bootstrapper = coroutineBootstrapper(mainContext) {
        // Run code collecting in BG
        codeFlow.map(::modelToAction)
            .onEach(this::dispatch)
            .launchIn(this)
    },
    executorFactory = coroutineExecutorFactory(mainContext) {
        onAction<GuardInstanceAction.CodeUpdated> { action ->
            dispatch(GuardInstanceMsg.CodeUpdated(code = action.code, progress = action.progress))
        }

        onIntent<GuardInstanceIntent.LoadActiveSessions> {
            launch {
                // Imitate load
                delay(1000L)

                dispatch(GuardInstanceMsg.SessionsUpdated(
                    sessions = listOf(
                        ActiveSession(
                            CAuthentication_RefreshToken_Enumerate_Response_RefreshTokenDescription(token_id = 1)
                        ), ActiveSession(
                            CAuthentication_RefreshToken_Enumerate_Response_RefreshTokenDescription(token_id = 2)
                        ), ActiveSession(
                            CAuthentication_RefreshToken_Enumerate_Response_RefreshTokenDescription(token_id = 3)
                        )
                    )
                ))
            }
        }

        onIntent<GuardInstanceIntent.LoadMobileConfirmations> {
            launch {
                // Imitate load
                delay(1000L)

                dispatch(GuardInstanceMsg.ConfirmationsUpdated(
                    listState = ConfirmationListState.Success(
                        conf = listOf()
                    )
                ))
            }
        }
    },
    reducer = { msg ->
        when (msg) {
            is GuardInstanceMsg.CodeUpdated -> copy(code = msg.code, codeProgress = msg.progress)
            is GuardInstanceMsg.ConfirmationsUpdated -> copy(confirmations = msg.listState)
            is GuardInstanceMsg.SessionsUpdated -> copy(sessions = msg.sessions.toPersistentList(), sessionsLoaded = true)
            is GuardInstanceMsg.RevocationCodeUpdated -> copy(revocationCode = msg.code)
        }
    }
)

// endregion

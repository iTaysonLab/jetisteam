package bruhcollective.itaysonlab.cobalt.guard.instance

import bruhcollective.itaysonlab.cobalt.core.CobaltDispatchers
import bruhcollective.itaysonlab.ksteam.ExtendedSteamClient
import bruhcollective.itaysonlab.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.ksteam.guard.models.MobileConfirmationItem
import bruhcollective.itaysonlab.ksteam.models.SteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.lifecycle.coroutines.withLifecycle
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnResume
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

internal class DefaultGuardInstanceComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    private val steamId: SteamId,
    private val onQrScannerClicked: () -> Unit,
    private val onRecoveryCodeClicked: (code: String) -> Unit,
    private val onDeleteClicked: () -> Unit,
    private val onSessionClicked: (ActiveSession) -> Unit,
    private val onConfirmationClicked: (MobileConfirmationItem) -> Unit,
): GuardInstanceComponent, ComponentContext by componentContext, KoinComponent {
    private val store = instanceKeeper.getStore {
        val instance = get<ExtendedSteamClient>().guard.instanceFor(steamId) ?: error("GuardInstanceStore cannot be constructed for a non-SG holding user")
        val codeFlow = instance.code.withLifecycle(lifecycle)

        GuardInstanceStore(
            storeFactory = storeFactory,
            mainContext = get<CobaltDispatchers>().main,
            steamClient = get<ExtendedSteamClient>(),
            codeFlow = codeFlow,
            initialState = GuardInstanceState(steamId = steamId, username = instance.username)
        )
    }

    init {
        lifecycle.doOnCreate {
            store.accept(GuardInstanceIntent.LoadActiveSessions)
        }

        lifecycle.doOnResume {
            store.accept(GuardInstanceIntent.LoadMobileConfirmations)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val state: StateFlow<GuardInstanceState> = store.stateFlow

    override fun openQrScanner() {
        onQrScannerClicked()
    }

    override fun openSessionDetail(session: ActiveSession) {
        onSessionClicked(session)
    }

    override fun openConfirmationDetail(confirmation: MobileConfirmationItem) {
        onConfirmationClicked(confirmation)
    }

    override fun openRecoveryCodeSheet() {
        onRecoveryCodeClicked(state.value.revocationCode)
    }

    override fun openDeleteSheet() {
        onDeleteClicked()
    }

    override fun reloadConfirmations() {
        store.accept(GuardInstanceIntent.LoadMobileConfirmations)
    }

    override fun reloadSessions() {
        store.accept(GuardInstanceIntent.LoadActiveSessions)
    }
}
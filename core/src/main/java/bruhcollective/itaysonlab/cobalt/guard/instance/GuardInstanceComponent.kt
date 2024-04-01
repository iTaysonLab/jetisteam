package bruhcollective.itaysonlab.cobalt.guard.instance

import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.ksteam.guard.models.MobileConfirmationItem
import kotlinx.coroutines.flow.StateFlow

interface GuardInstanceComponent {
    val state: StateFlow<GuardInstanceState>

    fun openQrScanner()
    fun openSessionDetail(session: ActiveSession)
    fun openConfirmationDetail(confirmation: MobileConfirmationItem)

    fun openRecoveryCodeSheet()
    fun openDeleteSheet()

    fun reloadConfirmations()
    fun reloadSessions()
}
package bruhcollective.itaysonlab.cobalt.guard.instance.confirmations

import bruhcollective.itaysonlab.ksteam.models.guard.ConfirmationListState
import bruhcollective.itaysonlab.ksteam.models.guard.MobileConfirmationItem
import com.arkivanov.decompose.value.Value

interface GuardConfirmationsComponent {
    val state: Value<ConfirmationListState>
    val isRefreshing: Value<Boolean>

    fun onRefresh()
    fun onConfirmationClicked(confirmation: MobileConfirmationItem)
}
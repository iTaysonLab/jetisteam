package bruhcollective.itaysonlab.cobalt.guard.instance.confirmations

import bruhcollective.itaysonlab.ksteam.guard.models.ConfirmationListState
import bruhcollective.itaysonlab.ksteam.guard.models.MobileConfirmationItem
import com.arkivanov.decompose.value.Value

interface GuardConfirmationsComponent {
    val state: Value<ConfirmationListState>
    val isRefreshing: Value<Boolean>

    fun onRefresh()
    fun onConfirmationClicked(confirmation: MobileConfirmationItem)
}
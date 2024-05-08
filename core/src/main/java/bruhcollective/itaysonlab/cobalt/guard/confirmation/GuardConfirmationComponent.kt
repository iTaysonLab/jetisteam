package bruhcollective.itaysonlab.cobalt.guard.confirmation

import com.arkivanov.decompose.value.Value

interface GuardConfirmationComponent {
    val confirmButton: String
    val cancelButton: String

    val detailsUrl: Value<String>
    val isConfirmationInProgress: Value<Boolean>
    val isCancellationInProgress: Value<Boolean>
    val isActionErrorOccurred: Value<Boolean>

    fun onConfirmClicked()
    fun onCancelClicked()
    fun onBackClicked()
    fun onErrorDismissed()
}
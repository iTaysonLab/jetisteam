package bruhcollective.itaysonlab.cobalt.guard.confirmation

import bruhcollective.itaysonlab.ksteam.ExtendedSteamClient
import bruhcollective.itaysonlab.ksteam.guard.models.MobileConfirmationItem
import bruhcollective.itaysonlab.ksteam.models.SteamId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnStart
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get

class DefaultGuardConfirmationComponent (
    private val item: MobileConfirmationItem,
    private val steamId: SteamId,
    private val onDismiss: () -> Unit,
    private val onActionTaken: () -> Unit,
    componentContext: ComponentContext
): GuardConfirmationComponent, KoinComponent, ComponentContext by componentContext, CoroutineScope by componentContext.coroutineScope() {
    private val steamClient: ExtendedSteamClient = get()

    override val confirmButton: String = item.acceptButtonText
    override val cancelButton: String = item.cancelButtonText

    override val detailsUrl = MutableValue("")
    override val isConfirmationInProgress = MutableValue(false)
    override val isCancellationInProgress = MutableValue(false)
    override val isActionErrorOccurred = MutableValue(false)

    init {
        lifecycle.doOnStart {
            launch {
                detailsUrl.value = steamClient.guardConfirmation.generateDetailPageUrl(
                    steamId = steamId,
                    item = item
                )
            }
        }
    }

    override fun onConfirmClicked() {
        isConfirmationInProgress.value = true
        dispatchAction(allow = true)
    }

    override fun onCancelClicked() {
        isCancellationInProgress.value = true
        dispatchAction(allow = false)
    }

    override fun onBackClicked() {
        onDismiss()
    }

    override fun onErrorDismissed() {
        isActionErrorOccurred.value = false
    }

    private fun dispatchAction(allow: Boolean) {
        isActionErrorOccurred.value = false

        launch {
            if (takeAction(allow = allow)) {
                onActionTaken()
            } else {
                isConfirmationInProgress.value = false
                isCancellationInProgress.value = false
                isActionErrorOccurred.value = true
            }
        }
    }

    private suspend fun takeAction(allow: Boolean): Boolean {
        return steamClient.guardConfirmation.setConfirmationStatus(
            steamId = steamId,
            item = item,
            allow = allow
        )
    }
}
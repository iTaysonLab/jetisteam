package bruhcollective.itaysonlab.microapp.guard.ui.confirmation_detail

import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.models.MobileConfirmationItem
import bruhcollective.itaysonlab.microapp.core.ext.getBase64
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.results.NavigationResult
import bruhcollective.itaysonlab.microapp.guard.GuardMicroapp
import bruhcollective.itaysonlab.microapp.guard.core.GuardConfirmationController
import bruhcollective.itaysonlab.microapp.guard.core.GuardController
import com.squareup.moshi.Moshi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.parcelize.Parcelize
import javax.inject.Inject

@HiltViewModel
internal class ConfirmationDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    guardController: GuardController,
    private val guardConfirmationController: GuardConfirmationController,
    moshi: Moshi
) : ViewModel() {
    val steamId = savedStateHandle.getSteamId()
    private val guardInstance = guardController.getInstance(steamId) ?: error("Confirmations should not be visible for steamId $steamId")

    val sessionData = moshi.adapter(MobileConfirmationItem::class.java).fromJson(
        savedStateHandle.getBase64(GuardMicroapp.Arguments.ConfirmationData.name)
            .string(Charsets.UTF_8)
    ) ?: error("Confirmation data is not provided for steamId $steamId")

    var webViewUrl by mutableStateOf("")
    private set

    var operationStatus by mutableStateOf<Operation>(Operation.Idle)
    private set

    init {
        viewModelScope.launch {
            webViewUrl = guardConfirmationController.generateDetailPageUrl(guardInstance, sessionData)
        }
    }

    fun submitAction(confirm: Boolean, onFinish: (ConfirmationDetailResult) -> Unit) {
        viewModelScope.launch {
            if (operationStatus != Operation.Idle) return@launch

            operationStatus = if (confirm) {
                Operation.Confirming
            } else {
                Operation.Revoking
            }

            guardConfirmationController.setConfirmationStatus(guardInstance, sessionData, confirm)

            onFinish(ConfirmationDetailResult(sessionData.id))
        }
    }

    sealed class Operation {
        object Idle: Operation()
        object Confirming: Operation()
        object Revoking: Operation()
    }

    @Stable
    @Parcelize
    class ConfirmationDetailResult(
        val id: String
    ): NavigationResult
}
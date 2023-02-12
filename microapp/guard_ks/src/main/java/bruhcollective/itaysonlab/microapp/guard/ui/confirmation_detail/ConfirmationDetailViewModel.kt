package bruhcollective.itaysonlab.microapp.guard.ui.confirmation_detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.ksteam.guard.models.MobileConfirmationItem
import bruhcollective.itaysonlab.ksteam.handlers.account
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.ksteam.handlers.guardConfirmation
import bruhcollective.itaysonlab.microapp.core.ext.getBase64
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.results.NavigationResult
import bruhcollective.itaysonlab.microapp.guard.GuardMicroapp
import bruhcollective.itaysonlab.microapp.guard.utils.ConfirmationDetailResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
internal class ConfirmationDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val steamClient: HostSteamClient,
) : ViewModel() {
    val steamId = savedStateHandle.getSteamId()
    private val guardInstance = steamClient.client.guard.instanceFor(steamId) ?: error("Confirmations should not be visible for steamId $steamId")

    @Suppress("JSON_FORMAT_REDUNDANT")
    val sessionData = Json {
        ignoreUnknownKeys = true
    }.decodeFromString<MobileConfirmationItem>(
        savedStateHandle.getBase64(GuardMicroapp.Arguments.ConfirmationData.name).string(Charsets.UTF_8)
    )

    var webViewUrl by mutableStateOf("")
    private set

    var operationStatus by mutableStateOf<Operation>(Operation.Idle)
    private set

    init {
        viewModelScope.launch {
            webViewUrl = steamClient.client.guardConfirmation.generateDetailPageUrl(guardInstance, sessionData)
        }
    }

    fun submitAction(confirm: Boolean, onFinish: (NavigationResult) -> Unit) {
        viewModelScope.launch {
            if (operationStatus != Operation.Idle) return@launch

            operationStatus = if (confirm) {
                Operation.Confirming
            } else {
                Operation.Revoking
            }

            steamClient.client.guardConfirmation.setConfirmationStatus(guardInstance, sessionData, confirm)

            onFinish(ConfirmationDetailResult(sessionData.id))
        }
    }

    fun buildSteamLoginSecureCookie(): String {
        return steamClient.client.account.buildSteamLoginSecureCookie()
    }

    sealed class Operation {
        object Idle: Operation()
        object Confirming: Operation()
        object Revoking: Operation()
    }
}
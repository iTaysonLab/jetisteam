package bruhcollective.itaysonlab.microapp.guard.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.jetisteam.controllers.JsLegacyController
import bruhcollective.itaysonlab.jetisteam.repository.TwoFactorRepository
import bruhcollective.itaysonlab.jetisteam.usecases.twofactor.GetQueueOfSessions
import bruhcollective.itaysonlab.jetisteam.usecases.twofactor.InitializeAddTfa
import bruhcollective.itaysonlab.microapp.guard.core.GuardController
import bruhcollective.itaysonlab.microapp.guard.core.GuardInstance
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import steam.twofactor.CTwoFactor_AddAuthenticator_Response
import javax.inject.Inject

@HiltViewModel
internal class GuardViewModel @Inject constructor(
    private val jsLegacyController: JsLegacyController,
    private val guardController: GuardController,
    private val initializeAddTfa: InitializeAddTfa,
    private val getQueueOfSessions: GetQueueOfSessions
): ViewModel() {
    val state by lazy {
        when (val instance = guardController.getInstance(jsLegacyController.steamId())) {
            null -> GuardState.Setup
            else -> GuardState.Available(instance = instance)
        }
    }

    var addState by mutableStateOf<AddGuardState>(AddGuardState.Noop)
        private set

    var isTryingToAddAuth by mutableStateOf(false)
        private set

    val steamId get() = jsLegacyController.steamId().steamId

    private var isPollingActive = state is GuardState.Available

    var confirmationFlow = flow {
        coroutineScope {
            while (isPollingActive) {
                emit(getQueueOfSessions())
                delay(5000L)
            }
        }
    }

    suspend fun addAuthenticator() {
        isTryingToAddAuth = true

        addState = when (val result = initializeAddTfa(jsLegacyController.steamId())) {
            is TwoFactorRepository.AddAuthenticatorResponse.WaitingForPhoneConfirmation -> AddGuardState.AwaitForSms(result.wrapped)
            TwoFactorRepository.AddAuthenticatorResponse.AlreadyExists -> AddGuardState.RequestToMove
        }

        isTryingToAddAuth = false
    }

    fun resetAddState() {
        addState = AddGuardState.Noop
    }

    sealed class GuardState {
        class Available(val instance: GuardInstance): GuardState()
        object Setup: GuardState()
    }

    sealed class AddGuardState {
        class AwaitForSms(val data: CTwoFactor_AddAuthenticator_Response): AddGuardState()
        object RequestToMove: AddGuardState()
        object Noop: AddGuardState()
    }
}
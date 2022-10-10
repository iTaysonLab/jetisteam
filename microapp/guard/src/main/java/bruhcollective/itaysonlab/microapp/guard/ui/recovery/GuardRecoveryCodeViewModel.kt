package bruhcollective.itaysonlab.microapp.guard.ui.recovery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.microapp.guard.GuardMicroappImpl
import bruhcollective.itaysonlab.microapp.guard.core.GuardController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class GuardRecoveryCodeViewModel @Inject constructor(
    guardController: GuardController,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val steamId = SteamID(savedStateHandle.get<String>(GuardMicroappImpl.InternalRoutes.ARG_STEAM_ID)!!.toLong())
    val revocationCode = guardController.getInstance(steamId)!!.revocationCode
}
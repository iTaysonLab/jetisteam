package bruhcollective.itaysonlab.microapp.guard.ui.recovery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.guard.GuardMicroappImpl
import bruhcollective.itaysonlab.microapp.guard.core.GuardController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class GuardRecoveryCodeViewModel @Inject constructor(
    guardController: GuardController,
    savedStateHandle: SavedStateHandle,
): ViewModel() {
    private val steamId = savedStateHandle.getSteamId(GuardMicroappImpl.InternalRoutes.ARG_STEAM_ID)
    val revocationCode = guardController.getInstance(steamId)!!.revocationCode
}
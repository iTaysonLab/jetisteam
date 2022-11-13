package bruhcollective.itaysonlab.microapp.guard.ui.recovery

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import bruhcollective.itaysonlab.microapp.guard.GuardMicroapp
import bruhcollective.itaysonlab.microapp.guard.core.GuardController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class GuardRecoveryCodeViewModel @Inject constructor(
    guardController: GuardController,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val steamId = savedStateHandle.getSteamId()
    val revocationCode = guardController.getInstance(steamId)!!.revocationCode
}
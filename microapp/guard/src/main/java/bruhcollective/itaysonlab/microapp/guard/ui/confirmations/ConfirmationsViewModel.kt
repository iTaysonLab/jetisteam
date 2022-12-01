package bruhcollective.itaysonlab.microapp.guard.ui.confirmations

import androidx.lifecycle.SavedStateHandle
import bruhcollective.itaysonlab.jetisteam.models.MobileConfGetList
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.guard.core.GuardConfirmationController
import bruhcollective.itaysonlab.microapp.guard.core.GuardController
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ConfirmationsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    guardController: GuardController,
    private val guardConfirmationController: GuardConfirmationController
) : PageViewModel<MobileConfGetList>() {
    val steamId = savedStateHandle.getSteamId()
    private val guardInstance = guardController.getInstance(steamId) ?: error("Confirmations should not be visible for steamId $steamId")

    init {
        reload()
    }

    override suspend fun load(): MobileConfGetList {
        return guardConfirmationController.getConfirmations(guardInstance)
    }
}
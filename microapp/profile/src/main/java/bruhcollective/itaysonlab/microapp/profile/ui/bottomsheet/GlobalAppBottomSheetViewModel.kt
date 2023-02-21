package bruhcollective.itaysonlab.microapp.profile.ui.bottomsheet

import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GlobalAppBottomSheetViewModel @Inject constructor(
    private val hostSteamClient: HostSteamClient
): ViewModel() {
    val steamId get() = hostSteamClient.currentSteamId
}
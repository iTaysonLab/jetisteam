package bruhcollective.itaysonlab.microapp.profile.ui.bottomsheet

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GlobalAppBottomSheetViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    val steamId = savedStateHandle.getSteamId()
}
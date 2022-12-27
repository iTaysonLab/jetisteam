package bruhcollective.itaysonlab.microapp.gamepage.ui.achievements

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.microapp.gamepage.GamePageMicroapp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameAchievementsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle
): ViewModel() {
    private val gameId = savedStateHandle.get<Int>(GamePageMicroapp.Arguments.GameId.name) ?: 0


}
package bruhcollective.itaysonlab.microapp.gamepage.ui

import androidx.lifecycle.SavedStateHandle
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.GetGamePage
import bruhcollective.itaysonlab.microapp.gamepage.GamePageMicroapp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GamePageViewModel @Inject constructor(
    private val getGamePage: GetGamePage,
    private val savedState: SavedStateHandle
) : PageViewModel<GetGamePage.GamePage>() {
    init { reload() }
    override suspend fun load() = getGamePage(savedState.get<Int>(GamePageMicroapp.GAME_ID)!!)
}
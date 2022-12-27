package bruhcollective.itaysonlab.microapp.gamepage.ui.achievements

import androidx.lifecycle.SavedStateHandle
import bruhcollective.itaysonlab.jetisteam.controllers.CdnController
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.game.GetGameAchievements
import bruhcollective.itaysonlab.microapp.gamepage.GamePageMicroapp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GameAchievementsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getGameAchievements: GetGameAchievements,
    private val cdnController: CdnController
): PageViewModel<GetGameAchievements.AchievementsPacked>() {
    private val gameId = savedStateHandle.get<Int>(GamePageMicroapp.Arguments.GameId.name) ?: 0

    init {
        reload()
    }

    fun wrapIcon(url: String): String? {
        return cdnController.publicImage("apps/$gameId/$url")
    }

    override suspend fun load() = getGameAchievements(gameId)
}
package bruhcollective.itaysonlab.microapp.profile.ui.screens.library

import androidx.lifecycle.SavedStateHandle
import bruhcollective.itaysonlab.jetisteam.mappers.OwnedGame
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.GetProfileOwnedGames
import bruhcollective.itaysonlab.microapp.profile.ProfileMicroappImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class LibraryScreenViewModel @Inject constructor(
    private val getProfileOwnedGames: GetProfileOwnedGames,
    private val savedState: SavedStateHandle
): PageViewModel<List<OwnedGame>>() {
    private var protoList = emptyList<OwnedGame>()

    init { reload() }

    override suspend fun load() = withContext(Dispatchers.Default) {
        getProfileOwnedGames(savedState.get<String>(ProfileMicroappImpl.InternalRoutes.ARG_ID)!!.toLong())
            .also { protoList = it }
            .sortedBy { it.name }
    }
}
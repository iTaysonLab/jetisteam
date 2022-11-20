package bruhcollective.itaysonlab.microapp.library.ui

import androidx.annotation.StringRes
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.SortByAlpha
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.mappers.OwnedGame
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.profile.GetProfileLibrary
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.library.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class LibraryScreenViewModel @Inject constructor(
    private val getProfileLibrary: GetProfileLibrary,
    savedState: SavedStateHandle
) : PageViewModel<List<OwnedGame>>() {
    private val steamId = savedState.getSteamId()
    private var oneOffJob: Job? = null

    var library by mutableStateOf(GetProfileLibrary.Library(emptyList(), emptyList()))
        private set

    var searchQuery by mutableStateOf(TextFieldValue(""))
    var sortMode by mutableStateOf(SortMode.ByName)

    val longSteamId get() = steamId.steamId

    init {
        reload()
    }

    override suspend fun load() = withContext(Dispatchers.Default) {
        getProfileLibrary(steamId)
            .also { library = it; }
            .games
            .sortedBy { it.proto.name }
    }

    fun notifySortReload() {
        oneOffJob?.cancel()
        oneOffJob = viewModelScope.launch(Dispatchers.Default) {
            setState(
                library.games
                    .filterIf(searchQuery.text.isNotEmpty()) { game ->
                        game.proto.name?.contains(searchQuery.text, ignoreCase = true) ?: false
                    }
                    .let { gameList ->
                        when (sortMode) {
                            SortMode.ByName -> gameList.sortedBy { it.proto.name }
                            SortMode.ByPlaytime -> gameList.sortedByDescending {
                                it.proto.playtime_forever ?: 0
                            }
                            SortMode.ByLaunchDate -> gameList.sortedByDescending {
                                it.proto.rtime_last_played ?: 0
                            }
                        }
                    }
            )

            oneOffJob = null
        }
    }

    enum class SortMode(@StringRes val nameRes: Int, val iconFunc: () -> ImageVector) {
        ByName(
            R.string.library_sort_name,
            { androidx.compose.material.icons.Icons.Rounded.SortByAlpha }),
        ByPlaytime(
            R.string.library_sort_time,
            { androidx.compose.material.icons.Icons.Rounded.Timer }),
        ByLaunchDate(
            R.string.library_sort_date,
            { androidx.compose.material.icons.Icons.Rounded.History }),
    }

    private inline fun <T> List<T>.filterIf(
        predicate: Boolean,
        crossinline func: (T) -> Boolean
    ): List<T> {
        return if (predicate) {
            this.filter(func)
        } else {
            this
        }
    }
}
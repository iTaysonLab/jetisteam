package bruhcollective.itaysonlab.microapp.library.ui.library_pages

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.ksteam.handlers.Library
import bruhcollective.itaysonlab.ksteam.handlers.Pics
import bruhcollective.itaysonlab.ksteam.handlers.Player
import bruhcollective.itaysonlab.ksteam.models.AppId
import bruhcollective.itaysonlab.ksteam.models.apps.AppSummary
import bruhcollective.itaysonlab.ksteam.models.library.OwnedGame
import bruhcollective.itaysonlab.ksteam.models.pics.AppInfo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class LibraryMainContainerViewModel @Inject constructor(
    private val steamLibrary: Library,
    private val steamPics: Pics,
    private val steamPlayer: Player
) : ViewModel() {
    val sortedUserCollections = steamLibrary.userCollections.map { collections ->
        collections.values.sortedWith { o1, o2 ->
            o1.name.compareTo(o2.name, ignoreCase = true)
        }.toImmutableList()
    }.stateIn(viewModelScope, SharingStarted.Eagerly, persistentListOf())

    val homeScreenShelves get() = steamLibrary.shelves.value

    val favorites: StateFlow<ImmutableList<AppSummary>> = steamLibrary.getFavoriteApps().map(List<AppSummary>::toImmutableList).stateIn(viewModelScope, SharingStarted.Eagerly, persistentListOf())
    val recentPlayed: StateFlow<ImmutableList<AppSummary>> = steamLibrary.getRecentApps().map(List<AppSummary>::toImmutableList).stateIn(viewModelScope, SharingStarted.Eagerly, persistentListOf())

    var nextPlayGames by mutableStateOf<ImmutableList<AppInfo>>(persistentListOf())
        private set

    init {
        viewModelScope.launch {
            nextPlayGames = steamPics.getAppIdsAsInfos(steamPlayer.getPlayNextQueue().map(
                ::AppId
            )).toImmutableList()
        }
    }

    fun getCollection(id: String) = steamLibrary.userCollections.mapNotNull { it[id] }
    fun getCollectionApps(id: String): Flow<ImmutableList<AppSummary>> = steamLibrary.getAppsInCollection(id).map(List<AppSummary>::toImmutableList)
    fun getCollectionOwnedApps(id: String): Flow<ImmutableList<OwnedGame>> = steamLibrary.getOwnedAppsInCollection(id).map(List<OwnedGame>::toImmutableList)
}
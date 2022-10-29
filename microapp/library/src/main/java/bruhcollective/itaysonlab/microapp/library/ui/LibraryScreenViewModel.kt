package bruhcollective.itaysonlab.microapp.library.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import bruhcollective.itaysonlab.jetisteam.mappers.OwnedGame
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.GetProfileLibrary
import bruhcollective.itaysonlab.microapp.core.ext.getLongFromString
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.library.LibraryMicroappImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import steam.clientcomm.CClientComm_GetAllClientLogonInfo_Response_Session
import javax.inject.Inject

@HiltViewModel
internal class LibraryScreenViewModel @Inject constructor(
    private val getProfileLibrary: GetProfileLibrary,
    private val savedState: SavedStateHandle
): PageViewModel<List<OwnedGame>>() {
    private val steamId = savedState.getSteamId(LibraryMicroappImpl.InternalRoutes.ARG_STEAM_ID)
    private var library = GetProfileLibrary.Library(emptyList(), emptyList())

    var machines by mutableStateOf<List<CClientComm_GetAllClientLogonInfo_Response_Session>>(emptyList())
        private set

    val longSteamId get() = steamId.steamId

    init { reload() }

    override suspend fun load() = withContext(Dispatchers.Default) {
        getProfileLibrary(steamId)
            .also { library = it; machines = it.machines; }
            .games
            .sortedBy { it.proto.name }
    }
}
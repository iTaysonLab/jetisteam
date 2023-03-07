package bruhcollective.itaysonlab.microapp.library.ui.game_page.community

import androidx.lifecycle.SavedStateHandle
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.ksteam.handlers.news
import bruhcollective.itaysonlab.ksteam.models.AppId
import bruhcollective.itaysonlab.ksteam.models.news.community.CommunityHubPost
import bruhcollective.itaysonlab.microapp.library.LibraryMicroapp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
internal class GamepageCommunityViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val hostSteamClient: HostSteamClient
): PageViewModel<List<CommunityHubPost>>() {
    private val appId = AppId(savedStateHandle.get<Int>(LibraryMicroapp.Arguments.ApplicationId.name) ?: 0)

    init {
        reload()
    }

    override suspend fun load() = withContext(Dispatchers.IO) {
        hostSteamClient.client.news.getCommunityHub(appId)
    }
}
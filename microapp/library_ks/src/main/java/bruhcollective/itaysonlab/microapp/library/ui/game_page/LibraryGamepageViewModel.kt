package bruhcollective.itaysonlab.microapp.library.ui.game_page

import androidx.lifecycle.SavedStateHandle
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.ksteam.handlers.pics
import bruhcollective.itaysonlab.ksteam.models.AppId
import bruhcollective.itaysonlab.ksteam.models.pics.AppInfo
import bruhcollective.itaysonlab.microapp.library.LibraryMicroapp
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LibraryGamepageViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val hostSteamClient: HostSteamClient
): PageViewModel<AppInfo>() {
    private val appId = AppId(savedStateHandle.get<Int>(LibraryMicroapp.Arguments.ApplicationId.name) ?: 0)

    init {
        reload()
    }

    override suspend fun load() = hostSteamClient.client.pics.getAppInfo(appId)
}
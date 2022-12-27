package bruhcollective.itaysonlab.microapp.steam_wrapped.ui

import androidx.lifecycle.SavedStateHandle
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.specials.GetSteamReplay
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class SteamReplayViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getSteamReplay: GetSteamReplay
): PageViewModel<GetSteamReplay.SteamReplay>() {
    val steamId = savedStateHandle.getSteamId()

    init {
        reload()
    }

    override suspend fun load() = getSteamReplay(steamId)
}
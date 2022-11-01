package bruhcollective.itaysonlab.microapp.library.ui.remote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.remote.GetRemoteMachineSummary
import bruhcollective.itaysonlab.microapp.core.ext.getLongFromString
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.library.LibraryMicroappImpl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteMachineViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val getRemoteMachineSummary: GetRemoteMachineSummary
): PageViewModel<GetRemoteMachineSummary.RemoteMachineSummary>() {
    private val steamId = savedState.getSteamId(LibraryMicroappImpl.InternalRoutes.ARG_STEAM_ID)
    private val machineId = savedState.getLongFromString(LibraryMicroappImpl.InternalRoutes.ARG_MACHINE_ID)

    init {
        viewModelScope.launch {
            reload()
        }
    }

    override suspend fun load(): GetRemoteMachineSummary.RemoteMachineSummary {
        return getRemoteMachineSummary(machineId)
    }
}
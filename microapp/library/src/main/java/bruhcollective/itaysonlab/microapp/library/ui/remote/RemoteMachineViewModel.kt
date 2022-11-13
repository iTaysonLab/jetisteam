package bruhcollective.itaysonlab.microapp.library.ui.remote

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.remote.GetRemoteMachineSummary
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.library.LibraryMicroapp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteMachineViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val getRemoteMachineSummary: GetRemoteMachineSummary
) : PageViewModel<GetRemoteMachineSummary.RemoteMachineSummary>() {
    private val steamId = savedState.getSteamId()
    private val machineId = savedState.get<Long>(LibraryMicroapp.Arguments.MachineId.name)!!

    init {
        viewModelScope.launch {
            reload()
        }
    }

    override suspend fun load(): GetRemoteMachineSummary.RemoteMachineSummary {
        return getRemoteMachineSummary(machineId)
    }
}
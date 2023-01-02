package bruhcollective.itaysonlab.microapp.library.ui.bottomsheet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.remote.GetRemoteInstallTargets
import bruhcollective.itaysonlab.jetisteam.usecases.remote.SetRemoteMachineDownloadState
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.library.LibraryMicroapp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
internal class InstallGameViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getRemoteInstallTargets: GetRemoteInstallTargets,
    private val setRemoteMachineDownloadState: SetRemoteMachineDownloadState,
): PageViewModel<GetRemoteInstallTargets.RemoteInstallTargetInfo>() {
    val steamId = savedStateHandle.getSteamId()
    val appId = savedStateHandle.get<Int>(LibraryMicroapp.Arguments.ApplicationId.name) ?: 0

    var isInstalling by mutableStateOf(false)
        private set

    init {
        viewModelScope.launch {
            reload()
        }
    }

    override suspend fun load(): GetRemoteInstallTargets.RemoteInstallTargetInfo {
        return getRemoteInstallTargets(appId)
    }

    fun installOn(machineId: Long, after: () -> Unit) {
        viewModelScope.launch {
            isInstalling = true
            setRemoteMachineDownloadState(machineId, appId, SetRemoteMachineDownloadState.Command.Install)
            after()
            // isInstalling = false
        }
    }
}
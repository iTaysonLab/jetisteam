package bruhcollective.itaysonlab.microapp.library.ui.remote

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.remote.GetRemoteMachineInstalledList
import bruhcollective.itaysonlab.jetisteam.usecases.remote.GetRemoteMachineSummary
import bruhcollective.itaysonlab.jetisteam.usecases.remote.SetRemoteMachineDownloadState
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.library.LibraryMicroapp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RemoteMachineViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val getRemoteMachineSummary: GetRemoteMachineSummary,
    private val getRemoteMachineInstalledList: GetRemoteMachineInstalledList,
    private val setRemoteMachineDownloadState: SetRemoteMachineDownloadState
) : PageViewModel<GetRemoteMachineInstalledList.RemoteMachineSummary>() {
    private var isPollingActive = true

    private val steamId = savedState.getSteamId()
    private val machineId = savedState.get<Long>(LibraryMicroapp.Arguments.MachineId.name)!!
    private var currentJob: Job? = null
    private var queueEntries = -1

    var currentJobId by mutableStateOf(0)
        private set

    var isUninstalling by mutableStateOf(false)
        private set

    var machineStateFlow = flow {
        coroutineScope {
            while (isPollingActive) {
                val data = runCatching {
                    getRemoteMachineSummary(machineId)
                }.getOrNull()

                if (data != null) {
                    val entryCount = (if (data.activeDownload != null) 1 else 0) + data.queueWaiting.size + data.queueCompleted.size

                    if (queueEntries != -1 && queueEntries != entryCount) {
                        setState(load()) // something is installed
                    }

                    queueEntries = entryCount

                    emit(data)
                    delay(1500L)
                } else {
                    // retry later
                    delay(500L)
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            reload()
        }
    }

    fun handleClick(appId: Int, command: SetRemoteMachineDownloadState.Command) {
        if (currentJob != null) return
        currentJob = viewModelScope.launch {
            currentJobId = appId
            setRemoteMachineDownloadState(machineId, appId, command)
            currentJobId = 0
            currentJob = null
        }
    }

    fun uninstallGame(appId: Int, after: () -> Unit) {
        if (currentJob != null) return
        currentJob = viewModelScope.launch {
            isUninstalling = true
            setRemoteMachineDownloadState(machineId, appId, SetRemoteMachineDownloadState.Command.Uninstall)
            setState(load())
            isUninstalling = false
            after()
            currentJob = null
        }
    }

    override suspend fun load(): GetRemoteMachineInstalledList.RemoteMachineSummary {
        return getRemoteMachineInstalledList(machineId)
    }

    override fun onCleared() {
        isPollingActive = false
    }
}
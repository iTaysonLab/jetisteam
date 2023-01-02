package bruhcollective.itaysonlab.jetisteam.usecases.remote

import bruhcollective.itaysonlab.jetisteam.repository.ClientCommRepository
import javax.inject.Inject

class SetRemoteMachineDownloadState @Inject constructor(
    private val commRepository: ClientCommRepository
) {
    enum class Command {
        CurrentPause,
        CurrentResume,
        QueueToTop,
        Uninstall
    }

    suspend operator fun invoke(
        machineId: Long,
        appId: Int,
        command: Command
    ) {
        when (command) {
            Command.CurrentPause, Command.CurrentResume -> commRepository.toggleActiveDownload(machineId, command == Command.CurrentResume)
            Command.QueueToTop -> commRepository.setClientAppUpdateState(machineId, appId, 1)
            Command.Uninstall -> commRepository.uninstall(machineId, appId)
        }
    }
}
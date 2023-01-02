package bruhcollective.itaysonlab.jetisteam.usecases.remote

import bruhcollective.itaysonlab.jetisteam.repository.ClientCommRepository
import steam.clientcomm.CClientComm_GetClientAppList_Response
import steam.clientcomm.CClientComm_GetClientAppList_Response_AppData
import javax.inject.Inject

class GetRemoteMachineInstalledList @Inject constructor(
    private val commRepository: ClientCommRepository
) {
    suspend operator fun invoke(
        machineId: Long
    ): RemoteMachineSummary {
        return RemoteMachineSummary(commRepository.getInstalledApps(clientId = machineId, withInfo = true, filters = "none"))
    }

    class RemoteMachineSummary(
        val installed: List<CClientComm_GetClientAppList_Response_AppData>,
        val notInstalled: List<CClientComm_GetClientAppList_Response_AppData>,
    ) {
        constructor(proto: CClientComm_GetClientAppList_Response): this(
            installed = proto.apps.filter { it.installed == true && it.bytes_downloaded == it.bytes_to_download && it.bytes_staged == it.bytes_to_stage },
            notInstalled = proto.apps.filter { it.installed == false }
        )
    }
}
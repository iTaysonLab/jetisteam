package bruhcollective.itaysonlab.jetisteam.usecases.remote

import bruhcollective.itaysonlab.jetisteam.repository.ClientCommRepository
import steam.clientcomm.CClientComm_ClientData
import steam.clientcomm.CClientComm_GetClientAppList_Response
import steam.clientcomm.CClientComm_GetClientAppList_Response_AppData
import javax.inject.Inject

class GetRemoteMachineSummary @Inject constructor(
    private val commRepository: ClientCommRepository
) {
    suspend operator fun invoke(
        machineId: Long
    ): RemoteMachineSummary {
        return RemoteMachineSummary(commRepository.getInstalledApps(clientId = machineId, withInfo = true, filters = "changing"))
    }

    class RemoteMachineSummary(
        val info: CClientComm_ClientData,
        val queue: List<CClientComm_GetClientAppList_Response_AppData>,
        val installed: List<CClientComm_GetClientAppList_Response_AppData>,
        val formattedBytesAvailable: String
    ) {
        constructor(proto: CClientComm_GetClientAppList_Response): this(
            info = proto.client_info!!,
            queue = proto.apps.filter { it.queue_position != null && it.changing == true },
            installed = proto.apps.filter { it.installed == true && it.bytes_downloaded == it.bytes_to_download && it.bytes_staged == it.bytes_to_stage },
            formattedBytesAvailable = proto.bytes_available.toString()
        )
    }
}
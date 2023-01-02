package bruhcollective.itaysonlab.jetisteam.usecases.remote

import bruhcollective.itaysonlab.jetisteam.repository.ClientCommRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import steam.clientcomm.CClientComm_ClientData
import steam.clientcomm.CClientComm_GetClientAppList_Response
import steam.clientcomm.CClientComm_GetClientAppList_Response_AppData
import javax.inject.Inject

class GetRemoteMachineSummary @Inject constructor(
    private val commRepository: ClientCommRepository
) {
    suspend operator fun invoke(
        machineId: Long
    ): RemoteMachineQueue {
        return withContext(Dispatchers.Default) {
            RemoteMachineQueue(commRepository.getInstalledApps(clientId = machineId, withInfo = true, filters = "changing"))
        }
    }

    class RemoteMachineQueue(
        val info: CClientComm_ClientData,
        val formattedBytesAvailable: Long,
        val activeDownload: CClientComm_GetClientAppList_Response_AppData?,
        val queueWaiting: List<CClientComm_GetClientAppList_Response_AppData>,
        val queueCompleted: List<CClientComm_GetClientAppList_Response_AppData>,
    ) {
        constructor(proto: CClientComm_GetClientAppList_Response): this(
            info = proto.client_info!!,
            formattedBytesAvailable = proto.bytes_available ?: 0L,
            queueWaiting = proto.apps.filter { it.queue_position != null && it.changing == true && it.queue_position != -1 }.sortedBy { it.queue_position }.drop(1),
            queueCompleted = proto.apps.filter { it.installed == true && it.bytes_downloaded == it.bytes_to_download && it.bytes_staged == it.bytes_to_stage },
            activeDownload = proto.apps.firstOrNull { it.queue_position == 0 },
        )
    }
}
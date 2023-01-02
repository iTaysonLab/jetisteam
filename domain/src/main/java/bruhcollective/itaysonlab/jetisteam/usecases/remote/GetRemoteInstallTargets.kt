package bruhcollective.itaysonlab.jetisteam.usecases.remote

import bruhcollective.itaysonlab.jetisteam.repository.ClientCommRepository
import bruhcollective.itaysonlab.jetisteam.repository.StoreRepository
import bruhcollective.itaysonlab.jetisteam.util.CdnUrlUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import steam.common.StoreBrowseItemDataRequest
import steam.common.StoreItemID
import javax.inject.Inject

class GetRemoteInstallTargets @Inject constructor(
    private val storeRepository: StoreRepository,
    private val commRepository: ClientCommRepository
) {
    suspend operator fun invoke(
        appId: Int
    ): RemoteInstallTargetInfo {
        return withContext(Dispatchers.Default) {
            val allClients = commRepository.getActiveClients().mapNotNull {
                it to commRepository.getInstalledApps(clientId = it.client_instanceid ?: return@mapNotNull null, withInfo = true)
            }

            val parsedClients = allClients.mapNotNull { pair ->
                val platformFreeSpace = pair.second.client_info?.bytes_available ?: 0

                val appInfo = pair.second.apps.firstOrNull {
                    it.appid == appId
                } ?: return@mapNotNull null

                InstallTarget(
                    machineId = pair.first.client_instanceid ?: 0,
                    machineName = pair.first.machine_name.orEmpty(),
                    osName = pair.first.os_name.orEmpty(),
                    freeSpace = platformFreeSpace,
                    requiredSpace = appInfo.bytes_required ?: 0,
                    status = when {
                        appInfo.available_on_platform != true -> InstallTargetStatus.NotSupportedOs
                        (appInfo.bytes_required ?: 0) > platformFreeSpace -> InstallTargetStatus.NoFreeSpace
                        appInfo.installed == true && appInfo.bytes_downloaded == appInfo.bytes_to_download && appInfo.bytes_staged == appInfo.bytes_to_stage -> InstallTargetStatus.Installed
                        appInfo.changing == true -> InstallTargetStatus.InProgress
                        else -> InstallTargetStatus.Ready
                    }
                )
            }

            val appInfo = storeRepository.getItems(
                ids = listOf(StoreItemID(appid = appId)),
                dataRequest = StoreBrowseItemDataRequest(
                    include_assets = true
                )
            ).store_items.firstOrNull()

            return@withContext RemoteInstallTargetInfo(
                appInfo = appInfo?.name.orEmpty().ifEmpty { "Unknown" } to CdnUrlUtil.buildCommunityUrl("images/apps/${appId}/${appInfo?.assets?.community_icon}.jpg"),
                targets = parsedClients
            )
        }
    }

    class RemoteInstallTargetInfo(
        val appInfo: Pair<String, String>,
        val targets: List<InstallTarget>
    )

    class InstallTarget(
        val machineId: Long,
        val machineName: String,
        val osName: String,
        val freeSpace: Long,
        val requiredSpace: Long,
        val status: InstallTargetStatus
    )

    enum class InstallTargetStatus {
        Ready,
        NotSupportedOs,
        NoFreeSpace,
        InProgress,
        Installed
    }
}
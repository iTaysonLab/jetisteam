package bruhcollective.itaysonlab.cobalt.core.ksteam

import android.content.Context
import bruhcollective.itaysonlab.ksteam.Core
import bruhcollective.itaysonlab.ksteam.Guard
import bruhcollective.itaysonlab.ksteam.Pics
import bruhcollective.itaysonlab.ksteam.debug.KSteamLoggingVerbosity
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.ksteam.kSteam
import bruhcollective.itaysonlab.ksteam.models.enums.EGamingDeviceType
import bruhcollective.itaysonlab.ksteam.network.CMClientState
import bruhcollective.itaysonlab.ksteam.platform.DeviceInformation
import bruhcollective.itaysonlab.ksteam.tryMigratingProtobufs
import com.tencent.mmkv.MMKV
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import okio.Path.Companion.toOkioPath
import steam.webui.authentication.EAuthTokenPlatformType
import java.io.File

class SteamClient (
    private val applicationContext: Context,
    private val mmkv: MMKV
): CoroutineScope by MainScope() + CoroutineName("Cobalt-SteamHolder") {
    private val deviceInformationController = DeviceInformationController(mmkv, applicationContext)

    val ksteam = kSteam {
        deviceInfo = DeviceInformation(
            osType = deviceInformationController.osType,
            gamingDeviceType = EGamingDeviceType.k_EGamingDeviceType_Phone,
            platformType = EAuthTokenPlatformType.k_EAuthTokenPlatformType_SteamClient,
            deviceName = deviceInformationController.deviceName
        )

        rootFolder = File(applicationContext.filesDir, "ksteam").toOkioPath()

        loggingTransport = KsteamAndroidLoggingTransport
        loggingVerbosity = KSteamLoggingVerbosity.Verbose

        ktor {
            HttpClient(OkHttp)
        }

        install(Core)

        install(Pics) {
            database = KsteamMmkvDatabase(mmkv)
        }

        install(Guard) {
            uuid = deviceInformationController.uuid
        }
    }

    val connectionStatus get() = ksteam.connectionStatus

    val isConnectedToSteam = ksteam.connectionStatus.map { state ->
        state == CMClientState.Connected
    }.stateIn(this, SharingStarted.WhileSubscribed(), false)

    val currentSteamId get() = ksteam.currentSessionSteamId

    init {
        launch {
            launchKsteam()
        }
    }

    private suspend fun launchKsteam() {
        ksteam.guard.tryMigratingProtobufs(ksteam)
        ksteam.start()
    }
}
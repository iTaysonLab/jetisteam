package bruhcollective.itaysonlab.cobalt.core.ksteam

import android.content.Context
import bruhcollective.itaysonlab.ksteam.KsteamClient
import bruhcollective.itaysonlab.ksteam.debug.AndroidLoggingTransport
import bruhcollective.itaysonlab.ksteam.debug.KSteamLoggingVerbosity
import bruhcollective.itaysonlab.ksteam.debug.PacketDumper
import bruhcollective.itaysonlab.ksteam.kSteam
import bruhcollective.itaysonlab.ksteam.models.enums.EGamingDeviceType
import bruhcollective.itaysonlab.ksteam.network.CMClientState
import bruhcollective.itaysonlab.ksteam.persistence.AndroidPersistenceDriver
import bruhcollective.itaysonlab.ksteam.platform.DeviceInformation
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.plus
import okio.Path.Companion.toOkioPath
import steam.enums.EAuthTokenPlatformType
import java.io.File
import java.util.concurrent.TimeUnit

class SteamClient (
    private val applicationContext: Context,
): CoroutineScope by MainScope() + CoroutineName("Cobalt-SteamHolder") {
    private val deviceInformationController = DeviceInformationController(applicationContext)

    val ksteam = kSteam {
        deviceInfo = DeviceInformation(
            osType = deviceInformationController.osType,
            gamingDeviceType = EGamingDeviceType.k_EGamingDeviceType_Phone,
            platformType = EAuthTokenPlatformType.k_EAuthTokenPlatformType_SteamClient,
            deviceName = deviceInformationController.deviceName
        )

        rootFolder = File(applicationContext.filesDir, "ksteam").toOkioPath()
        persistenceDriver = AndroidPersistenceDriver(applicationContext)

        loggingTransport = AndroidLoggingTransport
        loggingVerbosity = KSteamLoggingVerbosity.Verbose

        ktor {
            HttpClient(OkHttp) {
                engine {
                    config {
                        readTimeout(30, TimeUnit.SECONDS)
                        writeTimeout(30, TimeUnit.SECONDS)
                        connectTimeout(30, TimeUnit.SECONDS)
                    }
                }
            }
        }

        install(KsteamClient) {
            enablePics = true
        }
    }

    val connectionStatus get() = ksteam.connectionStatus

    val isConnectedToSteam = ksteam.connectionStatus.map { state ->
        state == CMClientState.Connected
    }.stateIn(this, SharingStarted.WhileSubscribed(), false)

    val currentSteamId get() = ksteam.currentSessionSteamId

    suspend fun start() {
        if (ksteam.connectionStatus.value == CMClientState.Offline) {
            ksteam.dumperMode = PacketDumper.DumpMode.Disable
            ksteam.start()
        }
    }
}
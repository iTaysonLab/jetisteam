package bruhcollective.itaysonlab.jetisteam

import android.content.Context
import bruhcollective.itaysonlab.jetisteam.controllers.UuidController
import bruhcollective.itaysonlab.jetisteam.data.BuildConfig
import bruhcollective.itaysonlab.jetisteam.util.KsteamAndroidLoggingTransport
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
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okio.Path.Companion.toOkioPath
import steam.webui.authentication.EAuthTokenPlatformType
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HostSteamClient @Inject constructor(
    @ApplicationContext context: Context,
    uuidController: UuidController,
    mmkvKvDatabase: MmkvKvDatabase,
): CoroutineScope by MainScope() {
    val client = kSteam {
        deviceInfo = DeviceInformation(
            osType = uuidController.osType,
            gamingDeviceType = EGamingDeviceType.k_EGamingDeviceType_Phone,
            platformType = EAuthTokenPlatformType.k_EAuthTokenPlatformType_SteamClient,
            deviceName = uuidController.deviceName
        )

        rootFolder = File(context.filesDir, "ksteam").toOkioPath()

        loggingTransport = KsteamAndroidLoggingTransport

        loggingVerbosity = if (BuildConfig.DEBUG) {
            KSteamLoggingVerbosity.Verbose
        } else {
            KSteamLoggingVerbosity.Warning
        }

        ktor {
            HttpClient(OkHttp)
        }

        install(Core)

        install(Pics) {
            database = mmkvKvDatabase
        }

        install(Guard) {
            uuid = uuidController.uuid
        }
    }

    val isConnectedToSteam = client.connectionStatus.map { state ->
        state == CMClientState.Connected
    }.stateIn(this, SharingStarted.WhileSubscribed(), false)

    val currentSteamId get() = client.currentSessionSteamId

    init {
        launch {
            client.guard.tryMigratingProtobufs(client)
            client.start()
        }
    }
}
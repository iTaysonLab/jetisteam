package bruhcollective.itaysonlab.jetisteam

import android.content.Context
import bruhcollective.itaysonlab.jetisteam.controllers.UuidController
import bruhcollective.itaysonlab.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.SteamClientConfiguration
import bruhcollective.itaysonlab.ksteam.models.enums.EGamingDeviceType
import bruhcollective.itaysonlab.ksteam.platform.DeviceInformation
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch
import steam.webui.authentication.EAuthTokenPlatformType
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class HostSteamClient @Inject constructor(
    @ApplicationContext context: Context,
    uuidController: UuidController
): CoroutineScope by MainScope() {
    val client = SteamClient(
        config = SteamClientConfiguration(
            deviceInfo = DeviceInformation(
                osType = uuidController.osType,
                gamingDeviceType = EGamingDeviceType.k_EGamingDeviceType_Phone,
                platformType = EAuthTokenPlatformType.k_EAuthTokenPlatformType_MobileApp,
                deviceName = uuidController.deviceName
            ), rootFolder = File(context.filesDir, "ksteam")
        )
    )

    init {
        launch {
            client.start()
        }
    }
}
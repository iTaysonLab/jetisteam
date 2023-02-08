package bruhcollective.itaysonlab.jetisteam

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import bruhcollective.itaysonlab.jetisteam.controllers.JsLegacyController
import bruhcollective.itaysonlab.jetisteam.controllers.UuidController
import bruhcollective.itaysonlab.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.SteamClientConfiguration
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.ksteam.models.SteamId
import bruhcollective.itaysonlab.ksteam.models.enums.EGamingDeviceType
import bruhcollective.itaysonlab.ksteam.persist.Database
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
    uuidController: UuidController,
    private val jsLegacyController: JsLegacyController
): CoroutineScope by MainScope() {
    val client = SteamClient(
        config = SteamClientConfiguration(
            deviceInfo = DeviceInformation(
                osType = uuidController.osType,
                gamingDeviceType = EGamingDeviceType.k_EGamingDeviceType_Phone,
                platformType = EAuthTokenPlatformType.k_EAuthTokenPlatformType_SteamClient,
                deviceName = uuidController.deviceName,
                uuid = uuidController.uuid
            ), rootFolder = File(context.filesDir, "ksteam"), sqlDriver = AndroidSqliteDriver(Database.Schema, context, "kSteamAndroid.db")
        )
    )

    init {
        launch {
            client.start()

            // Migrate info
            jsLegacyController.getGuard()?.let { newGuard ->
                client.guard.tryAddConfig(SteamId(newGuard.steam_id.toULong()), newGuard)
            }

            //if (jsLegacyController.authSession != null) {
            //    jsLegacyController.clearAuth()
            //}

            // client.account.trySignInSaved()
        }
    }
}
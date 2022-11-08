package bruhcollective.itaysonlab.jetisteam.controllers

import android.content.Context
import android.os.Build
import android.provider.Settings
import dagger.hilt.android.qualifiers.ApplicationContext
import steam.auth.EOSType
import java.util.UUID
import javax.inject.Inject

class UuidController @Inject constructor(
    configService: ConfigService,
    @ApplicationContext private val context: Context
) {
    val uuid by configService.LazyStringCfg(key = "app.uuid") { "android:${UUID.randomUUID()}" }

    val deviceName by lazy {
        return@lazy if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
            val deviceName = Settings.Global.getString(context.contentResolver, Settings.Global.DEVICE_NAME)
            if (deviceName == Build.MODEL) Build.MODEL else "$deviceName (${Build.MODEL})"
        } else {
            Build.MODEL
        }
    }

    val osType by lazy {
        return@lazy when (Build.VERSION.SDK_INT) {
            Build.VERSION_CODES.M -> EOSType.k_eAndroid6
            Build.VERSION_CODES.N, Build.VERSION_CODES.N_MR1 -> EOSType.k_eAndroid7
            Build.VERSION_CODES.O, Build.VERSION_CODES.O_MR1 -> EOSType.k_eAndroid8
            Build.VERSION_CODES.P -> EOSType.k_eAndroid9
            else -> EOSType.k_eAndroidUnknown
        }
    }
}
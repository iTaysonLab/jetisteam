package bruhcollective.itaysonlab.cobalt.core.ksteam

import android.content.Context
import android.os.Build
import android.provider.Settings
import bruhcollective.itaysonlab.ksteam.models.enums.EOSType
import com.tencent.mmkv.MMKV
import java.util.UUID

class DeviceInformationController (
    private val mmkv: MMKV,
    private val context: Context
) {
    val uuid: String
        get() {
            return if (mmkv.containsKey("app.uuid")) {
                mmkv.getString("app.uuid", null) ?: generateUuid()
            } else {
                generateUuid().also { newUuid ->
                    mmkv.putString("app.uuid", newUuid)
                }
            }
        }

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

    private fun generateUuid() = "android:${UUID.randomUUID()}"
}
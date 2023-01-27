package bruhcollective.itaysonlab.microapp.guard.utils

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Computer
import androidx.compose.material.icons.rounded.DeviceUnknown
import androidx.compose.material.icons.rounded.Language
import androidx.compose.material.icons.rounded.Smartphone
import androidx.compose.ui.graphics.vector.ImageVector
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.ksteam.guard.models.AwaitingSession
import steam.webui.authentication.EAuthTokenPlatformType

object SessionFormatter {
    fun formatSessionDescByTime(ctx: Context, desc: ActiveSession): SessionVisuals {
        return SessionVisuals((when (desc.platformType) {
            EAuthTokenPlatformType.k_EAuthTokenPlatformType_SteamClient -> "Steam Client" to { Icons.Rounded.Computer }
            EAuthTokenPlatformType.k_EAuthTokenPlatformType_WebBrowser -> "Web Browser" to { Icons.Rounded.Language }
            EAuthTokenPlatformType.k_EAuthTokenPlatformType_MobileApp -> "Mobile App" to { Icons.Rounded.Smartphone }
            else -> ("Unknown" to { Icons.Rounded.DeviceUnknown }).also { Log.d("Unknown", desc.toString()) }
        }) to DateUtils.getRelativeTimeSpanString(ctx,
            (desc.lastSeen?.time ?: desc.timeUpdated).toLong().times(1000L)
        ).toString())
    }

    fun formatAuthSession(desc: AwaitingSession): SessionVisuals {
        return SessionVisuals((when (desc.platformType) {
            EAuthTokenPlatformType.k_EAuthTokenPlatformType_SteamClient -> "Steam Client" to { Icons.Rounded.Computer }
            EAuthTokenPlatformType.k_EAuthTokenPlatformType_WebBrowser -> "Web Browser" to { Icons.Rounded.Language }
            EAuthTokenPlatformType.k_EAuthTokenPlatformType_MobileApp -> "Mobile App" to { Icons.Rounded.Smartphone }
            else -> ("Unknown" to { Icons.Rounded.DeviceUnknown }).also { Log.d("Unknown", desc.toString()) }
        }) to "")
    }

    @JvmInline
    value class SessionVisuals(private val packed: Triple<String, () -> ImageVector, String>) {
        val fallbackName: String get() = packed.first
        val icon: () -> ImageVector get() = packed.second
        val relativeLastSeen: String get() = packed.third
    }

    private infix fun <A, B, C> Pair<A, B>.to(third: C): Triple<A, B, C> = Triple(first, second, third)
}
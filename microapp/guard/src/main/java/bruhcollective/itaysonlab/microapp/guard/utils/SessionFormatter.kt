package bruhcollective.itaysonlab.microapp.guard.utils

import android.content.Context
import android.text.format.DateUtils
import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.ui.graphics.vector.ImageVector
import steam.auth.CAuthentication_RefreshToken_Enumerate_Response
import steam.auth.EAuthTokenPlatformType

object SessionFormatter {
    fun formatSessionDescByTime(ctx: Context, desc: CAuthentication_RefreshToken_Enumerate_Response.RefreshTokenDescription): SessionVisuals {
        return SessionVisuals((when (desc.platform_type) {
            EAuthTokenPlatformType.k_EAuthTokenPlatformType_SteamClient -> "Steam Client" to { Icons.Rounded.Computer }
            EAuthTokenPlatformType.k_EAuthTokenPlatformType_WebBrowser -> "Web Browser" to { Icons.Rounded.Language }
            EAuthTokenPlatformType.k_EAuthTokenPlatformType_MobileApp -> "Mobile App" to { Icons.Rounded.Smartphone }
            else -> ("Unknown" to { Icons.Rounded.DeviceUnknown }).also { Log.d("Unknown", desc.toString()) }
        }) to DateUtils.getRelativeTimeSpanString(ctx, (desc.last_seen?.time ?: desc.time_updated)?.toLong()?.times(1000L) ?: 0L).toString())
    }

    @JvmInline
    value class SessionVisuals(private val packed: Triple<String, () -> ImageVector, String>) {
        val fallbackName: String get() = packed.first
        val icon: () -> ImageVector get() = packed.second
        val relativeLastSeen: String get() = packed.third
    }

    private infix fun <A, B, C> Pair<A, B>.to(third: C): Triple<A, B, C> = Triple(first, second, third)
}
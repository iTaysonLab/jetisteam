package bruhcollective.itaysonlab.cobalt.guard

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
import bruhcollective.itaysonlab.ksteam.guard.models.IncomingSession
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import steam.enums.EAuthTokenPlatformType
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object GuardUtils {
    fun formatDateTimeToLocale(timestamp: Long): String {
        val dtf = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
        val date = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(TimeZone.UTC)
        return dtf.format(date.toJavaLocalDateTime())
    }

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

    fun formatSessionDescByTime(ctx: Context, platformType: EAuthTokenPlatformType, lastSeen: Int?): SessionVisuals {
        return SessionVisuals((when (platformType) {
            EAuthTokenPlatformType.k_EAuthTokenPlatformType_SteamClient -> "Steam Client" to { Icons.Rounded.Computer }
            EAuthTokenPlatformType.k_EAuthTokenPlatformType_WebBrowser -> "Web Browser" to { Icons.Rounded.Language }
            EAuthTokenPlatformType.k_EAuthTokenPlatformType_MobileApp -> "Mobile App" to { Icons.Rounded.Smartphone }
            else -> ("Unknown" to { Icons.Rounded.DeviceUnknown }).also { Log.d("Unknown", platformType.toString()) }
        }) to lastSeen?.toLong()?.times(1000L)?.let { DateUtils.getRelativeTimeSpanString(ctx, it).toString() })
    }

    fun formatAuthSession(desc: IncomingSession): SessionVisuals {
        return SessionVisuals((when (desc.platformType) {
            EAuthTokenPlatformType.k_EAuthTokenPlatformType_SteamClient -> "Steam Client" to { Icons.Rounded.Computer }
            EAuthTokenPlatformType.k_EAuthTokenPlatformType_WebBrowser -> "Web Browser" to { Icons.Rounded.Language }
            EAuthTokenPlatformType.k_EAuthTokenPlatformType_MobileApp -> "Mobile App" to { Icons.Rounded.Smartphone }
            else -> ("Unknown" to { Icons.Rounded.DeviceUnknown }).also { Log.d("Unknown", desc.toString()) }
        }) to "")
    }

    @JvmInline
    value class SessionVisuals(private val packed: Triple<String, () -> ImageVector, String?>) {
        val fallbackName: String get() = packed.first
        val icon: () -> ImageVector get() = packed.second
        val relativeLastSeen: String? get() = packed.third
    }

    private infix fun <A, B, C> Pair<A, B>.to(third: C): Triple<A, B, C> = Triple(first, second, third)
}
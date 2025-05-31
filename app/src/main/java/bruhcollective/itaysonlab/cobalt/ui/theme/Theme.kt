package bruhcollective.itaysonlab.cobalt.ui.theme

import android.os.Build
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.LocalActivity
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Accent,
    onPrimary = Color.Black,
    secondary = Accent,
    tertiary = Accent,
    background = Color.Black,
    surface = Color.Black,
    surfaceVariant = Color.White.copy(alpha = 0.15f).compositeOver(Color.Black),
    outline = Color.White.copy(alpha = 0.25f).compositeOver(Color.Black),
    outlineVariant = Color.White.copy(alpha = 0.35f).compositeOver(Color.Black),
    secondaryContainer = Color.White.copy(alpha = 0.35f).compositeOver(Color.Black),
    surfaceTint = Color.White,
    onSecondaryContainer = Color.White,
)

private val LightColorScheme = lightColorScheme()

@Composable
fun CobaltTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val activity = LocalActivity.current

    val colorScheme = when {
        darkTheme -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                dynamicDarkColorScheme(LocalContext.current)
            } else {
                DarkColorScheme
            }
        }

        else -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                dynamicLightColorScheme(LocalContext.current)
            } else {
                LightColorScheme
            }
        }
    }

    DisposableEffect(darkTheme) {
        (activity as? ComponentActivity)?.enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                android.graphics.Color.TRANSPARENT,
                android.graphics.Color.TRANSPARENT,
            ) { darkTheme },
            navigationBarStyle = SystemBarStyle.auto(
                lightScrim,
                darkScrim,
            ) { darkTheme },
        )

        onDispose {  }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CobaltTypography,
        content = content
    )
}


/**
 * The default light scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val lightScrim = android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

/**
 * The default dark scrim, as defined by androidx and the platform:
 * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
 */
private val darkScrim = android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b)
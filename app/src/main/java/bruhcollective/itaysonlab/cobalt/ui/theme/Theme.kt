package bruhcollective.itaysonlab.cobalt.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.systemuicontroller.rememberSystemUiController

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
    val sysUiController = rememberSystemUiController()

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

    SideEffect {
        sysUiController.setSystemBarsColor(color = Color.Transparent, darkIcons = colorScheme.background.luminance() > 0.5f)
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = CobaltTypography,
        content = content
    )
}
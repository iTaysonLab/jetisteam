package bruhcollective.itaysonlab.jetisteam.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver

val Accent = Color(0xFF29B6F6)

val ColorScheme.backgroundEmphasis @Composable get() = onSurface.copy(alpha = 0.1f).compositeOver(background)
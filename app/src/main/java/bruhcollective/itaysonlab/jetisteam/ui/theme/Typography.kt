package bruhcollective.itaysonlab.jetisteam.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.ui.font.robotoMonoFontFamily
import bruhcollective.itaysonlab.jetisteam.ui.font.rubikFontFamily

private val defaultTypography = Typography()

val CobaltTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = rubikFontFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = rubikFontFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = rubikFontFamily),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = robotoMonoFontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = robotoMonoFontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = robotoMonoFontFamily),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = rubikFontFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = rubikFontFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = rubikFontFamily),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = rubikFontFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = rubikFontFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = rubikFontFamily),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = robotoMonoFontFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = robotoMonoFontFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = robotoMonoFontFamily)
)
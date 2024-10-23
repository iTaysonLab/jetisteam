package bruhcollective.itaysonlab.cobalt.ui.theme

import androidx.compose.material3.Typography
import bruhcollective.itaysonlab.cobalt.ui.font.ibmPlexSerifFontFamily
import bruhcollective.itaysonlab.cobalt.ui.font.ibmPlexSansFontFamily

private val defaultTypography = Typography()

val CobaltTypography = Typography(
    displayLarge = defaultTypography.displayLarge.copy(fontFamily = ibmPlexSansFontFamily),
    displayMedium = defaultTypography.displayMedium.copy(fontFamily = ibmPlexSansFontFamily),
    displaySmall = defaultTypography.displaySmall.copy(fontFamily = ibmPlexSansFontFamily),

    headlineLarge = defaultTypography.headlineLarge.copy(fontFamily = ibmPlexSansFontFamily),
    headlineMedium = defaultTypography.headlineMedium.copy(fontFamily = ibmPlexSansFontFamily),
    headlineSmall = defaultTypography.headlineSmall.copy(fontFamily = ibmPlexSansFontFamily),

    titleLarge = defaultTypography.titleLarge.copy(fontFamily = ibmPlexSansFontFamily),
    titleMedium = defaultTypography.titleMedium.copy(fontFamily = ibmPlexSansFontFamily),
    titleSmall = defaultTypography.titleSmall.copy(fontFamily = ibmPlexSansFontFamily),

    bodyLarge = defaultTypography.bodyLarge.copy(fontFamily = ibmPlexSansFontFamily),
    bodyMedium = defaultTypography.bodyMedium.copy(fontFamily = ibmPlexSansFontFamily),
    bodySmall = defaultTypography.bodySmall.copy(fontFamily = ibmPlexSansFontFamily),

    labelLarge = defaultTypography.labelLarge.copy(fontFamily = ibmPlexSansFontFamily),
    labelMedium = defaultTypography.labelMedium.copy(fontFamily = ibmPlexSansFontFamily),
    labelSmall = defaultTypography.labelSmall.copy(fontFamily = ibmPlexSansFontFamily)
)
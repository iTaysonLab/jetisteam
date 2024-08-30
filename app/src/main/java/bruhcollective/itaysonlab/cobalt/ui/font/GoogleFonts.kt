package bruhcollective.itaysonlab.cobalt.ui.font

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import bruhcollective.itaysonlab.cobalt.R

internal val googleFontsProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

internal val robotoMonoGoogleFont = GoogleFont("Roboto Mono")
internal val rubikGoogleFont = GoogleFont("Rubik")

val rubikFontFamily = FontFamily(
    Font(googleFont = rubikGoogleFont, fontProvider = googleFontsProvider),
    Font(googleFont = rubikGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.Bold)
)

val robotoMonoFontFamily = FontFamily(
    Font(googleFont = robotoMonoGoogleFont, fontProvider = googleFontsProvider),
    Font(googleFont = robotoMonoGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.Bold)
)
package bruhcollective.itaysonlab.cobalt.ui.font

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import bruhcollective.itaysonlab.cobalt.R

internal val googleFontsProvider = GoogleFont.Provider(
    providerAuthority = "com.google.android.gms.fonts",
    providerPackage = "com.google.android.gms",
    certificates = R.array.com_google_android_gms_fonts_certs
)

internal val ibmPlexSansGoogleFont = GoogleFont("IBM Plex Sans")
internal val ibmPlexSerifGoogleFont = GoogleFont("IBM Plex Serif")
internal val robotoMonoGoogleFont = GoogleFont("Roboto Mono")

val ibmPlexSansFontFamily = FontFamily(
    Font(googleFont = ibmPlexSansGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.Normal),
    Font(googleFont = ibmPlexSansGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.Normal, style = FontStyle.Italic),
    Font(googleFont = ibmPlexSansGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.Medium),
    Font(googleFont = ibmPlexSansGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.SemiBold)
)

val ibmPlexSerifFontFamily = FontFamily(
    Font(googleFont = ibmPlexSerifGoogleFont, fontProvider = googleFontsProvider),
    Font(googleFont = ibmPlexSerifGoogleFont, fontProvider = googleFontsProvider, weight = FontWeight.SemiBold)
)

val robotoMonoFontFamily = FontFamily(
    Font(googleFont = robotoMonoGoogleFont, fontProvider = googleFontsProvider),
)
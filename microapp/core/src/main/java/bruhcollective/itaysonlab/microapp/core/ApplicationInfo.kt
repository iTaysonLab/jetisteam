package bruhcollective.itaysonlab.microapp.core

import androidx.compose.runtime.staticCompositionLocalOf

data class ApplicationInfo (
    val versionNumber: String,
    val versionCode: Int
)

val LocalApplicationInfo = staticCompositionLocalOf<ApplicationInfo> { error("Provide ApplicationInfo in microapp host module!") }
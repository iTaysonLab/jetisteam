package bruhcollective.itaysonlab.jetisteam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import bruhcollective.itaysonlab.jetisteam.ui.screens.AppNavigation
import bruhcollective.itaysonlab.jetisteam.ui.theme.JetisteamTheme
import bruhcollective.itaysonlab.microapp.core.ApplicationInfo
import bruhcollective.itaysonlab.microapp.core.LocalApplicationInfo
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.delegates.LocalShareDispatcher
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.delegates.ShareDispatcher
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val shareDispatcher = ShareDispatcher(this)

    private val localApplicationInfo = ApplicationInfo(
        versionCode = BuildConfig.VERSION_CODE,
        versionNumber = BuildConfig.VERSION_NAME
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            CompositionLocalProvider(LocalShareDispatcher provides shareDispatcher, LocalApplicationInfo provides localApplicationInfo) {
                JetisteamTheme {
                    AppNavigation()
                }
            }
        }
    }
}
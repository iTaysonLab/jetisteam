package bruhcollective.itaysonlab.jetisteam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.jetisteam.ui.components.SteamConnectionRow
import bruhcollective.itaysonlab.jetisteam.ui.theme.CobaltTheme
import com.arkivanov.decompose.defaultComponentContext
import org.koin.android.ext.android.inject

class MainActivity : ComponentActivity() {
    private val steamClient: SteamClient by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        val rootComponent = AndroidCobaltComponent(defaultComponentContext())

        setContent {
            CobaltTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column {
                        val connectionStatus by steamClient.connectionStatus.collectAsState()
                        var connectionVisible by remember { mutableStateOf(true) }

                        SteamConnectionRow(
                            connectionState = connectionStatus,
                            onVisibilityChanged = { connectionVisible = it }
                        )

                        CobaltScreen(connectionVisible, rootComponent)
                    }
                }
            }
        }
    }
}
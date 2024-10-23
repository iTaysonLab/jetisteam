package bruhcollective.itaysonlab.cobalt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.ui.theme.CobaltTheme
import com.arkivanov.decompose.defaultComponentContext
import com.arkivanov.decompose.extensions.compose.stack.animation.LocalStackAnimationProvider
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.StackAnimationProvider
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import soup.compose.material.motion.MotionConstants

class MainActivity : ComponentActivity(), CoroutineScope by MainScope() {
    private val steamClient: SteamClient by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        startSteamClient()

        val rootComponent = createCobaltComponent()

        setContent {
            CompositionLocalProvider(
                LocalStackAnimationProvider provides DefaultStackAnimationProvider
            ) {
                CobaltTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        CobaltScreen(rootComponent)
                    }
                }
            }
        }
    }

    private fun startSteamClient() {
        launch {
            steamClient.start()
        }
    }

    private fun createCobaltComponent(): AndroidCobaltComponent {
        return AndroidCobaltComponent(
            componentContext = defaultComponentContext()
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    private object DefaultStackAnimationProvider : StackAnimationProvider {
        private val spec: FiniteAnimationSpec<Float> = tween(durationMillis = MotionConstants.DefaultMotionDuration, easing = FastOutSlowInEasing)

        override fun <C : Any, T : Any> provide(): StackAnimation<C, T> = stackAnimation(fade(spec) + slide(spec))
    }
}

fun cobaltStackAnimationSpec() = tween<Float>(durationMillis = MotionConstants.DefaultMotionDuration, easing = FastOutSlowInEasing)
fun cobaltStackAnimator() = fade(cobaltStackAnimationSpec()) + slide(cobaltStackAnimationSpec())
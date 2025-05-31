package bruhcollective.itaysonlab.cobalt.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.staticCompositionLocalOf

/**
 * Consumes "Scroll to Top" events.
 */
@Stable
internal class ScrollToTopConsumer {
    private var callbacks = mutableListOf<ScrollToTopCallback>()

    fun registerCallback(callback: ScrollToTopCallback) {
        println("ScrollToTopConsumer: add $callback")
        callbacks.add(callback)
    }

    fun removeCallback(callback: ScrollToTopCallback) {
        println("ScrollToTopConsumer: remove $callback")
        callbacks.remove(callback)
    }

    fun dispatchScrollToTop(): Boolean {
        println("ScrollToTopConsumer: dispatchScrollToTop")

        if (callbacks.isNotEmpty()) {
            for (callback in callbacks) {
                if (callback.enabled) {
                    callback.handleOnScrollToTop()
                }
            }

            return true
        } else {
            return false
        }
    }
}

internal abstract class ScrollToTopCallback (
    var enabled: Boolean
) {
    abstract fun handleOnScrollToTop()
}

internal val LocalScrollToTopConsumer = staticCompositionLocalOf { ScrollToTopConsumer() }

@Composable
internal fun ScrollToTopHandler(enabled: Boolean = true, onScrollToTop: () -> Unit) {
    val currentLambdaCallback by rememberUpdatedState(onScrollToTop)

    val actualCallback = remember {
        object: ScrollToTopCallback(enabled) {
            override fun handleOnScrollToTop() {
                currentLambdaCallback.invoke()
            }
        }
    }

    SideEffect {
        actualCallback.enabled = enabled
    }

    val consumer = LocalScrollToTopConsumer.current
    DisposableEffect(Unit) {
        consumer.registerCallback(actualCallback)
        onDispose { consumer.removeCallback(actualCallback) }
    }
}
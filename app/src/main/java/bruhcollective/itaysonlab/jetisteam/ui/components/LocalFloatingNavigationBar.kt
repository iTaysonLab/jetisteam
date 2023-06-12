package bruhcollective.itaysonlab.jetisteam.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.unit.dp
import kotlin.math.absoluteValue

object FloatingNavigationBarHolder {
    var shouldNavigationBarBeVisible by mutableStateOf(true)
        internal set
}

val LocalFullscreenBottomInsets = staticCompositionLocalOf { 0.dp }

@Composable
fun rememberFloatingNavigationBarScrollConnection(): NestedScrollConnection {
    return remember {
        object: NestedScrollConnection {
            private var scrolledUntilTrigger = 0f
            private var scrolledUntilTriggerConst = 200f

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                scrolledUntilTrigger += available.y

                if (scrolledUntilTrigger.absoluteValue >= scrolledUntilTriggerConst) {
                    if (scrolledUntilTrigger < 0) {
                        // forward, hide
                        FloatingNavigationBarHolder.shouldNavigationBarBeVisible = false
                    } else if (scrolledUntilTrigger > 0) {
                        // backward, show
                        FloatingNavigationBarHolder.shouldNavigationBarBeVisible = true
                    }

                    // reset
                    scrolledUntilTrigger = 0f
                }

                return Offset.Zero
            }
        }
    }
}
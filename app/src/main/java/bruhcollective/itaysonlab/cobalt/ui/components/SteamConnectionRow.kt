package bruhcollective.itaysonlab.cobalt.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.ksteam.network.CMClientState
import kotlinx.coroutines.delay
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.rememberSlideDistance

@Composable
fun SteamConnectionRow (
    connectionState: CMClientState,
    overrideVisibility: SteamConnectionRowVisibility = SteamConnectionRowVisibility.Default,
    onVisibilityChanged: (Boolean) -> Unit = {},
) {
    val sd = rememberSlideDistance()
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(connectionState, overrideVisibility) {
        if (overrideVisibility != SteamConnectionRowVisibility.Default) {
            (overrideVisibility == SteamConnectionRowVisibility.AlwaysShow).let {
                isVisible = it
                onVisibilityChanged(it)
            }

            return@LaunchedEffect
        }

        isVisible = true
        onVisibilityChanged(true)

        if (connectionState == CMClientState.Connected) {
            delay(1000L)
            isVisible = false
            onVisibilityChanged(false)
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        modifier = Modifier.fillMaxWidth(),
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
    ) {
        Column(modifier = Modifier.background(MaterialTheme.colorScheme.surfaceContainer).fillMaxWidth()) {
            AnimatedContent(targetState = connectionState, transitionSpec = {
                materialSharedAxisY(true, slideDistance = sd).using(SizeTransform(clip = false))
            }, modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 16.dp), label = "") {
                Row(horizontalArrangement = Arrangement.Start, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    when (it) {
                        CMClientState.Offline -> {
                            ProgressRow(title = "Preparing...")
                        }

                        CMClientState.Connecting -> {
                            ProgressRow(title = "Connecting to Steam network...")
                        }

                        CMClientState.Authorizing -> {
                            ProgressRow(title = "Logging in...")
                        }

                        CMClientState.Reconnecting -> {
                            ProgressRow(title = "Reconnecting...")
                        }

                        CMClientState.Connected -> {
                            Icon(imageVector = Icons.Rounded.Check, modifier = Modifier.size(16.dp), contentDescription = null)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "Connected!")
                        }

                        CMClientState.Error -> TODO()

                        CMClientState.AwaitingAuthorization -> {
                            ProgressRow(title = "Awaiting sign in...")
                        }
                    }
                }
            }

            CobaltDivider(padding = 0.dp)
        }
    }
}

@Composable
private fun ProgressRow(title: String) {
    ResizableCircularIndicator(indicatorSize = 16.dp, strokeWidth = 2.dp)
    Spacer(modifier = Modifier.width(12.dp))
    Text(text = title)
}

enum class SteamConnectionRowVisibility {
    Default, AlwaysShow, AlwaysHide
}
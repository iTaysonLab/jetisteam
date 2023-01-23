package bruhcollective.itaysonlab.jetisteam.ui

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.uikit.components.ResizableCircularIndicator
import bruhcollective.itaysonlab.ksteam.network.CMClientState
import kotlinx.coroutines.delay
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.rememberSlideDistance

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SteamConnectionRow (
    connectionState: CMClientState
) {
    val sd = rememberSlideDistance()
    var isVisible by remember { mutableStateOf(true) }

    LaunchedEffect(connectionState) {
        isVisible = true

        if (connectionState == CMClientState.Connected) {
            delay(1000L)
            isVisible = false
        }
    }

    AnimatedVisibility(
        visible = isVisible,
        modifier = Modifier.fillMaxWidth(),
        enter = fadeIn() + expandVertically(expandFrom = Alignment.Top),
        exit = shrinkVertically(shrinkTowards = Alignment.Top) + fadeOut(),
    ) {
        Box(modifier = Modifier
            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp))
            .padding(16.dp)
            .fillMaxWidth()
            .statusBarsPadding()) {

            AnimatedContent(targetState = connectionState, transitionSpec = {
                materialSharedAxisY(true, slideDistance = sd).using(SizeTransform(clip = false))
            }, modifier = Modifier.fillMaxWidth()) {
                Row(horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    when (it) {
                        CMClientState.Idle -> {
                            ProgressRow(title = "Preparing...")
                        }

                        CMClientState.Connecting -> {
                            ProgressRow(title = "Connecting to Steam network...")
                        }

                        CMClientState.Logging -> {
                            ProgressRow(title = "Logging in...")
                        }

                        CMClientState.Connected -> {
                            Icon(imageVector = Icons.Rounded.Check, modifier = Modifier.size(16.dp), contentDescription = null)
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(text = "Connected!")
                        }

                        CMClientState.Error -> TODO()
                    }
                }
            }
        }
    }
}

@Composable
private fun ProgressRow(title: String) {
    ResizableCircularIndicator(indicatorSize = 16.dp, strokeWidth = 2.dp)
    Spacer(modifier = Modifier.width(12.dp))
    Text(text = title)
}
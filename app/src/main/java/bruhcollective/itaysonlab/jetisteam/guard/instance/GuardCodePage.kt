package bruhcollective.itaysonlab.jetisteam.guard.instance

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ContentCopy
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Password
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.R
import bruhcollective.itaysonlab.jetisteam.ui.font.robotoMonoFontFamily
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.rememberSlideDistance

@Composable
internal fun GuardCodePage(
    code: String,
    codeProgress: Float,
    onCopyClicked: () -> Unit,
    onRecoveryClicked: () -> Unit,
    onDeleteClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    val slideDistance = rememberSlideDistance()

    val intProgress by animateFloatAsState(
        targetValue = codeProgress, animationSpec = tween(1000, easing = LinearEasing),
        label = ""
    )

    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .width(IntrinsicSize.Min),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            AnimatedContent(
                targetState = code, transitionSpec = {
                    materialSharedAxisY(
                        forward = true,
                        slideDistance = slideDistance
                    ).using(
                        SizeTransform(clip = false)
                    )
                }
            ) { code ->
                Text(
                    fontFamily = robotoMonoFontFamily,
                    text = code,
                    textAlign = TextAlign.Center,
                    fontSize = 56.sp,
                    letterSpacing = 12.sp,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            LinearProgressIndicator(
                progress = { intProgress },
                modifier = Modifier.fillMaxWidth(),
                // trackColor = MaterialTheme.colorScheme.primary,
                drawStopIndicator = null
            )

            IconButton(
                onClick = onCopyClicked,
            ) {
                Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = stringResource(id = R.string.guard_actions_copy))
            }
        }

        Row(
            modifier = Modifier.align(Alignment.BottomStart).padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = onDeleteClicked
            ) {
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = stringResource(id = R.string.guard_actions_remove))
            }

            IconButton(
                onClick = onRecoveryClicked
            ) {
                Icon(imageVector = Icons.Rounded.Password, contentDescription = stringResource(id = R.string.guard_recovery))
            }
        }
    }
}
package bruhcollective.itaysonlab.cobalt.guard.instance

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.guard.instance.code.GuardCodeComponent
import bruhcollective.itaysonlab.cobalt.ui.font.robotoMonoFontFamily
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import soup.compose.material.motion.animation.materialSharedAxisY
import soup.compose.material.motion.animation.rememberSlideDistance

@Composable
internal fun GuardCodePage(
    component: GuardCodeComponent
) {
    val code by component.code.subscribeAsState()
    val codeProgress by component.codeProgress.subscribeAsState()

    val slideDistance = rememberSlideDistance()
    val clipboardManager = LocalClipboardManager.current

    val intProgress by animateFloatAsState(
        targetValue = codeProgress, animationSpec = tween(1000, easing = LinearEasing),
        label = ""
    )

    Box(
        modifier = Modifier.fillMaxSize()
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
                drawStopIndicator = { }
            )

            IconButton(
                onClick = {
                    clipboardManager.setText(AnnotatedString(code))
                },
            ) {
                Icon(imageVector = Icons.Rounded.ContentCopy, contentDescription = stringResource(id = R.string.guard_actions_copy))
            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            IconButton(
                onClick = component::onDeleteGuardButtonClicked
            ) {
                Icon(imageVector = Icons.Rounded.Delete, contentDescription = stringResource(id = R.string.guard_actions_remove))
            }

            IconButton(
                onClick = component::onRecoveryCodeButtonClicked
            ) {
                Icon(imageVector = Icons.Rounded.Password, contentDescription = stringResource(id = R.string.guard_recovery))
            }
        }
    }
}
package bruhcollective.itaysonlab.microapp.library.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Update
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.uikit.FloatingWindowInsetsAsPaddings
import bruhcollective.itaysonlab.ksteam.handlers.Pics
import bruhcollective.itaysonlab.microapp.library.R

@Composable
fun LibraryLoadingPage(
    picsState: Pics.PicsState
) {
    val progress by animateFloatAsState(targetValue = when (picsState) {
        Pics.PicsState.Initialization -> 0f
        Pics.PicsState.UpdatingPackages -> 0.25f
        Pics.PicsState.UpdatingApps -> 0.75f
        Pics.PicsState.Ready -> 1f
    }, animationSpec = spring(stiffness = 650F))

    Box(modifier = Modifier
        .fillMaxSize()
        .statusBarsPadding()
        .padding(FloatingWindowInsetsAsPaddings)) {
        Column(
            modifier = Modifier.align(Alignment.Center),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Rounded.Update,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(56.dp)
            )

            Text(
                text = stringResource(id = R.string.library_pics_wait_title),
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                style = MaterialTheme.typography.headlineSmall
            )

            Text(
                text = stringResource(id = R.string.library_pics_wait_text),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LinearProgressIndicator(
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.5f)
                    .clip(CircleShape),
                progress = progress,
            )

            Text(
                text = stringResource(id = when (picsState) {
                    Pics.PicsState.Initialization -> R.string.library_pics_wait_task_preparing
                    Pics.PicsState.UpdatingPackages -> R.string.library_pics_wait_task_packages
                    Pics.PicsState.UpdatingApps -> R.string.library_pics_wait_task_apps
                    Pics.PicsState.Ready -> R.string.library_pics_wait_task_ready
                }),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
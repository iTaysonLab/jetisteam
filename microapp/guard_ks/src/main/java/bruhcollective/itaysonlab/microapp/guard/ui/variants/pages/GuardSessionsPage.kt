package bruhcollective.itaysonlab.microapp.guard.ui.variants.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenLoading
import bruhcollective.itaysonlab.jetisteam.uikit.partialShapes
import bruhcollective.itaysonlab.jetisteam.uikit.floatingWindowInsetsAsPaddings
import bruhcollective.itaysonlab.microapp.guard.R
import bruhcollective.itaysonlab.microapp.guard.ui.GuardViewModel
import bruhcollective.itaysonlab.microapp.guard.utils.SessionFormatter
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun GuardSessionsPage(
    modifier: Modifier,
    sessions: ImmutableList<GuardViewModel.ActiveSessionWrapper>?,
    onSessionClicked: (GuardViewModel.ActiveSessionWrapper) -> Unit
) {
    RoundedPage(modifier) {
        if (sessions != null) {
            LazyColumn(contentPadding = floatingWindowInsetsAsPaddings(16.dp), modifier = Modifier) {
                itemsIndexed(sessions, key = { _, session ->
                    session.session.id
                }, contentType = { _, _ ->
                    0
                }) { index, session ->
                    SessionItem(session, top = index == 0, bottom = index == sessions.lastIndex, onClick = {
                        onSessionClicked(session)
                    })

                    Divider(color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                }
            }
        } else {
            FullscreenLoading()
        }
    }
}

@Composable
private fun SessionItem(
    session: GuardViewModel.ActiveSessionWrapper,
    top: Boolean,
    bottom: Boolean,
    onClick: () -> Unit
) {
    val ctx = LocalContext.current
    val visuals = remember(session) { SessionFormatter.formatSessionDescByTime(ctx, session.session) }

    ListItem(
        headlineContent = {
            Text(text = remember(session) {
                session.session.deviceName.ifEmpty { visuals.fallbackName }
            }, maxLines = 1)
        }, leadingContent = {
            Icon(imageVector = visuals.icon(), contentDescription = null)
        }, supportingContent = {
            Text(text = stringResource(id = R.string.guard_sessions_last_seen, visuals.relativeLastSeen), maxLines = 1)
        }, colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp)
        ), modifier = Modifier.clip(when {
            top -> MaterialTheme.partialShapes.largeTopShape
            bottom -> MaterialTheme.partialShapes.largeBottomShape
            else -> RectangleShape
        }).clickable(onClick = onClick)
    )
}
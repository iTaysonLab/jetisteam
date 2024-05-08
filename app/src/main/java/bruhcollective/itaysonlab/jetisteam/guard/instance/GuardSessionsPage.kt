package bruhcollective.itaysonlab.jetisteam.guard.instance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
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
import bruhcollective.itaysonlab.jetisteam.R
import bruhcollective.itaysonlab.jetisteam.guard.GuardUtils
import bruhcollective.itaysonlab.jetisteam.ui.components.FullscreenLoading
import bruhcollective.itaysonlab.jetisteam.ui.theme.partialShapes
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import kotlinx.collections.immutable.ImmutableList

@Composable
internal fun GuardSessionsPage(
    isRefreshing: Boolean,
    currentSession: ActiveSession?,
    sessions: ImmutableList<ActiveSession>,
    onSessionClicked: (ActiveSession) -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
) {
    if (isRefreshing.not()) {
        LazyColumn(
            contentPadding = contentPadding,
            state = listState,
            modifier = modifier
        ) {
            if (currentSession != null) {
                item(key = currentSession.id) {
                    SessionItem(
                        currentSession,
                        top = true,
                        bottom = true,
                        onClick = {
                            onSessionClicked(currentSession)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            itemsIndexed(sessions, key = { _, session ->
                session.id
            }) { index, session ->
                SessionItem(
                    session,
                    top = index == 0,
                    bottom = index == sessions.lastIndex,
                    onClick = {
                        onSessionClicked(session)
                    }
                )

                if (index != sessions.lastIndex) {
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                }
            }
        }
    } else {
        FullscreenLoading()
    }
}

@Composable
private fun SessionItem(
    session: ActiveSession,
    top: Boolean,
    bottom: Boolean,
    onClick: () -> Unit
) {
    val ctx = LocalContext.current
    val visuals = remember(session) { GuardUtils.formatSessionDescByTime(ctx, session) }

    ListItem(
        headlineContent = {
            Text(text = remember(session) {
                session.deviceName.ifEmpty { visuals.fallbackName }
            }, maxLines = 1)
        }, leadingContent = {
            Icon(imageVector = visuals.icon(), contentDescription = null)
        }, supportingContent = if (session.isCurrentSession) {
            {
                Text(
                    text = stringResource(R.string.guard_current_session)
                )
            }
        } else if (visuals.relativeLastSeen != null) {
            {
                Text(
                    text = stringResource(
                        id = R.string.guard_sessions_last_seen,
                        visuals.relativeLastSeen!!
                    ), maxLines = 1
                )
            }
        } else null, colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                elevation = if (session.isCurrentSession) 4.dp else 16.dp
            )
        ), modifier = Modifier
            .clip(
                when {
                    top && bottom -> MaterialTheme.shapes.large
                    top -> MaterialTheme.partialShapes.largeTopShape
                    bottom -> MaterialTheme.partialShapes.largeBottomShape
                    else -> RectangleShape
                }
            )
            .clickable(onClick = onClick)
    )
}
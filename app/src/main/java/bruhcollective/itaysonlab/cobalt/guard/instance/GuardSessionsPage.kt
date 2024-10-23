package bruhcollective.itaysonlab.cobalt.guard.instance

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.guard.GuardUtils
import bruhcollective.itaysonlab.cobalt.guard.instance.sessions.GuardSessionsComponent
import bruhcollective.itaysonlab.cobalt.ui.components.FullscreenLoading
import bruhcollective.itaysonlab.cobalt.ui.theme.partialShapes
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardSessionsPage(
    component: GuardSessionsComponent,
) {
    val sessions by component.sessions.subscribeAsState()
    val currentSession by component.currentSession.subscribeAsState()
    val isRefreshing by component.isRefreshing.subscribeAsState()
    val isLoading by component.isLoading.subscribeAsState()

    if (isLoading.not()) {
        PullToRefreshBox(isRefreshing = isRefreshing, onRefresh = component::onRefresh) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp)
            ) {
                item(key = currentSession.id) {
                    SessionItem(
                        currentSession,
                        top = true,
                        bottom = true,
                        onClick = {
                            component.onSessionClicked(currentSession)
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                }

                itemsIndexed(sessions, key = { _, session -> session.id }) { index, session ->
                    SessionItem(
                        session,
                        top = index == 0,
                        bottom = index == sessions.lastIndex,
                        onClick = {
                            component.onSessionClicked(session)
                        }
                    )

                    if (index != sessions.lastIndex) {
                        HorizontalDivider(color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
                    }
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
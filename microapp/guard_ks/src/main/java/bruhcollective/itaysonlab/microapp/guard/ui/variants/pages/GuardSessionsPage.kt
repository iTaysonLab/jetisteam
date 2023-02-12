package bruhcollective.itaysonlab.microapp.guard.ui.variants.pages

import android.annotation.SuppressLint
import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenLoading
import bruhcollective.itaysonlab.ksteam.guard.models.ActiveSession
import bruhcollective.itaysonlab.microapp.guard.R
import bruhcollective.itaysonlab.microapp.guard.utils.SessionFormatter

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
internal fun GuardSessionsPage(
    modifier: Modifier,
    sessions: List<ActiveSession>?,
    onSessionClicked: (ActiveSession) -> Unit
) {

    RoundedPage(modifier) {
        if (sessions != null) {
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                itemsIndexed(sessions) { index, session ->
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionItem(
    session: ActiveSession,
    top: Boolean,
    bottom: Boolean,
    onClick: () -> Unit
) {
    val ctx = LocalContext.current
    val visuals = remember(session) { SessionFormatter.formatSessionDescByTime(ctx, session) }

    ListItem(
        headlineText = {
            Text(text = remember(session) {
                session.deviceName.ifEmpty { visuals.fallbackName }
            }, maxLines = 1)
        }, leadingContent = {
            Icon(imageVector = visuals.icon(), contentDescription = null)
        }, supportingText = {
            Text(text = stringResource(id = R.string.guard_sessions_last_seen, visuals.relativeLastSeen), maxLines = 1)
        }, colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ), modifier = Modifier.clip(when {
            top -> MaterialTheme.shapes.large.copy(bottomStart = CornerSize(0.dp), bottomEnd = CornerSize(0.dp))
            bottom -> MaterialTheme.shapes.large.copy(topStart = CornerSize(0.dp), topEnd = CornerSize(0.dp))
            else -> RectangleShape
        }).clickable(onClick = onClick)
    )
}
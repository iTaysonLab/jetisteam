package bruhcollective.itaysonlab.microapp.guard.ui.session_detail

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.components.StateTextButton
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.guard.R
import bruhcollective.itaysonlab.microapp.guard.utils.SessionFormatter
import solaricons.bold.SolarIconsBold
import solaricons.bold.solariconsbold.EssentionalUi
import solaricons.bold.solariconsbold.essentionalui.Help

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardSessionScreen(
    viewModel: GuardSessionViewModel = hiltViewModel(),
    onBackClicked: () -> Unit,
) {
    val ctx = LocalContext.current

    var requestSessionRevoke by remember { mutableStateOf(false) }

    if (requestSessionRevoke) {
        AlertDialog(onDismissRequest = {
            if (!viewModel.revokingSession) {
                requestSessionRevoke = false
            }
        }, icon = {
            Icon(
                imageVector = Icons.Rounded.Delete,
                contentDescription = null
            )
        }, title = {
            Text(text = stringResource(id = R.string.guard_revoke_alert_title))
        }, text = {
            Text(text = stringResource(id = R.string.guard_revoke_alert_text))
        }, confirmButton = {
            StateTextButton(onClick = {
                viewModel.requestRevokeSession {
                    requestSessionRevoke = false
                    onBackClicked()
                }
            }, inLoadingState = viewModel.revokingSession) {
                Text(text = stringResource(id = R.string.guard_revoke_alert_confirm))
            }
        }, dismissButton = {
            TextButton(onClick = {
                if (!viewModel.revokingSession) {
                    requestSessionRevoke = false
                }
            }) {
                Text(text = stringResource(id = R.string.guard_revoke_alert_dismiss))
            }
        })
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = {
                Text(text = stringResource(id = R.string.guard_session_info))
            }, navigationIcon = {
                IconButton(onClick = onBackClicked) {
                    Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                }
            }, colors = TopAppBarDefaults.largeTopAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                scrolledContainerColor = MaterialTheme.colorScheme.surface,
            ))
        }, contentWindowInsets = EmptyWindowInsets, modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        RoundedPage(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            LazyColumn {
                item {
                    val visuals = remember(viewModel.sessionData) {
                        SessionFormatter.formatSessionDescByTime(
                            ctx,
                            viewModel.sessionData
                        )
                    }

                    SessionHeader(
                        modifier = Modifier.fillMaxWidth(),
                        icon = visuals.icon,
                        title = visuals.fallbackName,
                        text = stringResource(
                            id = R.string.guard_sessions_last_seen,
                            visuals.relativeLastSeen
                        )
                    )
                }

                item {
                    SessionActionStrip(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 4.dp), onRevokeRequest = {
                            requestSessionRevoke = true
                        }
                    )
                }

                items(viewModel.infoBlocks) { info ->
                    ListItem(
                        headlineText = {
                            Text(text = stringResource(id = info.titleRes), maxLines = 1)
                        }, leadingContent = {
                            Icon(imageVector = info.icon(), contentDescription = null)
                        }, supportingText = {
                            Text(text = info.text)
                        }, colors = ListItemDefaults.colors(
                            containerColor = Color.Transparent
                        )
                    )

                    Divider(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

@Composable
private fun SessionHeader(
    modifier: Modifier,
    icon: () -> ImageVector,
    title: String,
    text: String
) {
    Column(modifier = modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            imageVector = icon(),
            tint = MaterialTheme.colorScheme.onPrimaryContainer,
            contentDescription = null,
            modifier = Modifier
                .clip(CircleShape)
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(8.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = title, style = MaterialTheme.typography.headlineSmall)

        Spacer(modifier = Modifier.height(4.dp))

        Text(text = text)
    }
}

@Composable
private fun SessionActionStrip(
    onRevokeRequest: () -> Unit,
    modifier: Modifier
) {
    val uriHandler = LocalUriHandler.current

    Card(
        modifier, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(16.dp)
        ), shape = MaterialTheme.shapes.large
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            /*SessionActionStripButton(
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onRevokeRequest),
                icon = Icons.Rounded.Delete,
                text = stringResource(id = R.string.guard_session_info_action_revoke)
            )

            Box(
                Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
            )*/

            SessionActionStripButton(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        // TODO: maybe add some sort of WebView
                        uriHandler.openUri("https://help.steampowered.com/")
                    },
                icon = SolarIconsBold.EssentionalUi.Help,
                text = stringResource(id = R.string.guard_session_info_action_support)
            )
        }
    }
}

@Composable
private fun SessionActionStripButton(
    modifier: Modifier,
    text: String,
    icon: ImageVector
) {
    Column(
        modifier = modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            tint = MaterialTheme.colorScheme.primary,
            contentDescription = null,
            modifier = Modifier
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = text, color = MaterialTheme.colorScheme.primary
        )
    }
}
package bruhcollective.itaysonlab.jetisteam.guard.session

import android.text.format.DateUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Help
import androidx.compose.material.icons.rounded.Key
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Router
import androidx.compose.material.icons.rounded.TextFormat
import androidx.compose.material.icons.rounded.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.guard.session.GuardSessionDetailComponent
import bruhcollective.itaysonlab.jetisteam.R
import bruhcollective.itaysonlab.jetisteam.guard.GuardUtils
import bruhcollective.itaysonlab.jetisteam.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.jetisteam.ui.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.ui.components.StateTextButton
import steam.enums.EAuthSessionGuardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardSessionScreen(
    component: GuardSessionDetailComponent
) {
    val ctx = LocalContext.current

    var requestSessionRevoke by remember { mutableStateOf(false) }

    if (requestSessionRevoke) {
        AlertDialog(onDismissRequest = {
            // if (!viewModel.revokingSession) {
            requestSessionRevoke = false
            // }
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
                // viewModel.requestRevokeSession {
                requestSessionRevoke = false
                //    onBackClicked()
                //}
            }, inLoadingState = false) {
                Text(text = stringResource(id = R.string.guard_revoke_alert_confirm))
            }
        }, dismissButton = {
            TextButton(onClick = {
                // if (!viewModel.revokingSession) {
                requestSessionRevoke = false
                // }
            }) {
                Text(text = stringResource(id = R.string.guard_revoke_alert_dismiss))
            }
        })
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.guard_session_info))
            }, navigationIcon = {
                IconButton(onClick = component::onBackClicked) {
                    Icon(imageVector = Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = null)
                }
            })
        },
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = EmptyWindowInsets
    ) { innerPadding ->
        RoundedPage(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        ) {
            LazyColumn {
                item {
                    val visuals = remember(ctx, component.platformType, component.lastSeen) {
                        GuardUtils.formatSessionDescByTime(
                            ctx,
                            component.platformType,
                            component.lastSeen
                        )
                    }

                    SessionHeader(
                        modifier = Modifier.fillMaxWidth(),
                        icon = visuals.icon,
                        title = visuals.fallbackName,
                        text = visuals.relativeLastSeen?.let {
                            stringResource(id = R.string.guard_sessions_last_seen, it)
                        }
                    )
                }

                item {
                    SessionActionStrip(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .padding(bottom = 4.dp),
                        onRevokeRequest = component::revokeSession
                    )
                }

                item {
                    SessionListItem(
                        headlineContent = {
                            Text(text = stringResource(id = R.string.guard_session_info_desc))
                        }, leadingContent = {
                            Icon(
                                imageVector = Icons.Rounded.TextFormat,
                                contentDescription = stringResource(id = R.string.guard_session_info_desc)
                            )
                        }, supportingContent = {
                            Text(text = component.deviceName)
                        }
                    )
                }

                if (component.firstSeen != null) {
                    item {
                        SessionListItem(
                            headlineContent = {
                                Text(text = stringResource(id = R.string.guard_session_info_first))
                            }, leadingContent = {
                                Icon(
                                    imageVector = Icons.Rounded.Timer,
                                    contentDescription = stringResource(id = R.string.guard_session_info_first)
                                )
                            }, supportingContent = {
                                Text(text = remember(ctx, component.firstSeen) {
                                    DateUtils.getRelativeTimeSpanString(
                                        ctx,
                                        component.firstSeen!!.toLong().times(1000L)
                                    ).toString()
                                })
                            }
                        )
                    }
                }

                item {
                    SessionListItem(
                        headlineContent = {
                            Text(text = stringResource(id = R.string.guard_session_info_signed))
                        }, leadingContent = {
                            Icon(
                                imageVector = Icons.Rounded.Key,
                                contentDescription = stringResource(id = R.string.guard_session_info_signed)
                            )
                        }, supportingContent = {
                            Text(
                                text = when (component.guardType) {
                                    EAuthSessionGuardType.k_EAuthSessionGuardType_None -> "None"
                                    EAuthSessionGuardType.k_EAuthSessionGuardType_EmailCode -> "Email (code)"
                                    EAuthSessionGuardType.k_EAuthSessionGuardType_DeviceCode -> "Steam Guard (code)"
                                    EAuthSessionGuardType.k_EAuthSessionGuardType_DeviceConfirmation -> "Steam Guard (mobile)"
                                    EAuthSessionGuardType.k_EAuthSessionGuardType_EmailConfirmation -> "Email (confirmation)"
                                    EAuthSessionGuardType.k_EAuthSessionGuardType_MachineToken -> "Machine token"
                                    else -> "Unknown"
                                }
                            )
                        }
                    )
                }

                if (component.lastSeenLocationString != null) {
                    item {
                        SessionListItem(
                            headlineContent = {
                                Text(text = stringResource(id = R.string.guard_session_info_loc))
                            }, leadingContent = {
                                Icon(
                                    imageVector = Icons.Rounded.LocationOn,
                                    contentDescription = stringResource(id = R.string.guard_session_info_loc)
                                )
                            }, supportingContent = {
                                Text(text = component.lastSeenLocationString!!)
                            }
                        )
                    }
                }

                if (component.lastSeenIp != null) {
                    item {
                        SessionListItem(
                            headlineContent = {
                                Text(text = stringResource(id = R.string.guard_session_info_ip))
                            }, leadingContent = {
                                Icon(
                                    imageVector = Icons.Rounded.Router,
                                    contentDescription = stringResource(id = R.string.guard_session_info_ip)
                                )
                            }, supportingContent = {
                                Text(text = component.lastSeenIp!!)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun SessionListItem(
    headlineContent: @Composable () -> Unit,
    supportingContent: @Composable () -> Unit,
    leadingContent: @Composable () -> Unit,
) {
    ListItem(
        headlineContent = headlineContent,
        leadingContent = leadingContent,
        supportingContent = supportingContent,
        colors = ListItemDefaults.colors(containerColor = Color.Transparent)
    )

    HorizontalDivider(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
private fun SessionHeader(
    modifier: Modifier,
    icon: () -> ImageVector,
    title: String,
    text: String?
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

        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            textAlign = TextAlign.Center
        )

        if (text != null) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = text)
        }
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
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
        ), shape = MaterialTheme.shapes.large
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.height(IntrinsicSize.Min)
        ) {
            SessionActionStripButton(
                modifier = Modifier.weight(1f).clickable(onClick = onRevokeRequest),
                icon = Icons.Rounded.Delete,
                text = stringResource(id = R.string.guard_session_info_action_revoke)
            )

            Box(
                Modifier
                    .fillMaxHeight()
                    .width(2.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))
            )

            SessionActionStripButton(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        // TODO: maybe add some sort of WebView
                        uriHandler.openUri("https://help.steampowered.com/")
                    },
                icon = Icons.Rounded.Help,
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
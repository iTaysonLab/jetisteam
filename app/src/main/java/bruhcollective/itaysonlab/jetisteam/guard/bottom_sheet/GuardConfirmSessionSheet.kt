package bruhcollective.itaysonlab.jetisteam.guard.bottom_sheet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.Router
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardIncomingSessionComponent
import bruhcollective.itaysonlab.jetisteam.R
import bruhcollective.itaysonlab.jetisteam.guard.GuardUtils
import bruhcollective.itaysonlab.jetisteam.ui.components.BottomSheetLayout
import bruhcollective.itaysonlab.jetisteam.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.jetisteam.ui.components.StateButton
import bruhcollective.itaysonlab.jetisteam.ui.components.StateTonalButton
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardConfirmSessionSheet(
    component: GuardIncomingSessionComponent
) {
    val isConfirmationInProgress by component.isConfirmationInProgress.subscribeAsState()
    val isCancellationInProgress by component.isCancellationInProgress.subscribeAsState()

    val state by component.state.subscribeAsState()
    val shouldRememberPassword by component.shouldRememberPassword.subscribeAsState()

    ModalBottomSheet(
        onDismissRequest = component::cancelSession,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
        contentWindowInsets = { EmptyWindowInsets }
    ) {
        BottomSheetLayout(title = {
            stringResource(id = R.string.guard_confirm_sheet_header)
        }, subtitle = {
            buildAnnotatedString {
                append(stringResource(id = R.string.guard_as))
                append(" ")
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                    append(component.username)
                }
            }
        }) {
            when (val s = state) {
                GuardIncomingSessionComponent.State.Loading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                is GuardIncomingSessionComponent.State.Ready -> {
                    Card(Modifier.padding(horizontal = 16.dp), shape = MaterialTheme.shapes.large) {
                        Column {
                            val visuals = remember(s.session) {
                                GuardUtils.formatAuthSession(s.session)
                            }

                            ListItem(
                                leadingContent = {
                                    Icon(
                                        imageVector = visuals.icon(),
                                        contentDescription = null
                                    )
                                },
                                headlineContent = {
                                    Text(text = visuals.fallbackName)
                                },
                                colors = ListItemDefaults.colors(
                                    leadingIconColor = MaterialTheme.colorScheme.primary,
                                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                                ),
                                supportingContent = {
                                    Text(text = remember(s.session) {
                                        s.session.deviceName.ifEmpty { "Unknown name" }
                                    })
                                }
                            )

                            Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                            ListItem(
                                leadingContent = {
                                    Icon(
                                        imageVector = Icons.Rounded.LocationOn,
                                        contentDescription = null
                                    )
                                },
                                headlineContent = {
                                    Text(text = stringResource(id = R.string.guard_confirm_sheet_location))
                                },
                                colors = ListItemDefaults.colors(
                                    leadingIconColor = MaterialTheme.colorScheme.primary,
                                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                                ),
                                supportingContent = {
                                    Text(text = remember(s) {
                                        "${s.session.city}, ${s.session.state}, ${s.session.country}"
                                    })
                                }
                            )

                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                            ListItem(
                                leadingContent = {
                                    Icon(imageVector = Icons.Rounded.Router, contentDescription = null)
                                },
                                headlineContent = {
                                    Text(text = stringResource(id = R.string.guard_confirm_sheet_ip))
                                },
                                colors = ListItemDefaults.colors(
                                    leadingIconColor = MaterialTheme.colorScheme.primary,
                                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                                ),
                                supportingContent = {
                                    Text(text = s.session.ip)
                                }
                            )

                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                            ListItem(
                                leadingContent = {
                                    Icon(imageVector = Icons.Rounded.Save, contentDescription = null)
                                },
                                headlineContent = {
                                    Text(text = stringResource(id = R.string.guard_confirm_sheet_remember))
                                },
                                modifier = Modifier.clickable(onClick = {
                                    component.setShouldRememberPassword(!shouldRememberPassword)
                                }),
                                colors = ListItemDefaults.colors(
                                    leadingIconColor = MaterialTheme.colorScheme.primary,
                                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                                ),
                                supportingContent = {
                                    Text(text = stringResource(id = R.string.guard_confirm_sheet_remember_subtitle))
                                },
                                trailingContent = {
                                    Switch(
                                        checked = shouldRememberPassword,
                                        onCheckedChange = {
                                            component.setShouldRememberPassword(it)
                                        })
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        StateTonalButton(
                            onClick = component::cancelSession,
                            inLoadingState = isCancellationInProgress,
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(16.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Text(text = stringResource(id = R.string.guard_confirm_sheet_action_deny), color = MaterialTheme.colorScheme.onSecondaryContainer)
                        }

                        StateButton(
                            onClick = component::confirmSession,
                            inLoadingState = isConfirmationInProgress,
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(16.dp),
                            shape = MaterialTheme.shapes.large
                        ) {
                            Text(text = stringResource(id = R.string.guard_confirm_sheet_action_approve), color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}
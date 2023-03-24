package bruhcollective.itaysonlab.microapp.guard.ui.bottomsheet

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.jetisteam.uikit.components.*
import bruhcollective.itaysonlab.ksteam.guard.models.AwaitingSession
import bruhcollective.itaysonlab.ksteam.handlers.guard
import bruhcollective.itaysonlab.ksteam.handlers.guardManagement
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.results.NavigationResult
import bruhcollective.itaysonlab.microapp.guard.GuardMicroapp
import bruhcollective.itaysonlab.microapp.guard.R
import bruhcollective.itaysonlab.microapp.guard.utils.ConfirmedNewSession
import bruhcollective.itaysonlab.microapp.guard.utils.SessionFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardConfirmSessionSheet(
    viewModel: GuardConfirmSessionViewModel = hiltViewModel(),
    onFinish: (NavigationResult) -> Unit
) {
    BottomSheetLayout(title = {
        stringResource(id = R.string.guard_confirm_sheet_header)
    }, subtitle = {
        buildAnnotatedString {
            append(stringResource(id = R.string.guard_as))
            append(" ")
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                append(viewModel.guardInstance.username)
            }
        }
    }) {
        when (val state = viewModel.state) {
            GuardConfirmSessionViewModel.State.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            is GuardConfirmSessionViewModel.State.Ready -> {
                Card(Modifier.padding(horizontal = 16.dp), shape = MaterialTheme.shapes.large) {
                    Column {
                        val visuals = remember(state.session) {
                            SessionFormatter.formatAuthSession(state.session)
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
                            modifier = Modifier.clickable(onClick = { }),
                            colors = ListItemDefaults.colors(
                                leadingIconColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                            ),
                            supportingContent = {
                                Text(text = remember(state.session) {
                                    state.session.deviceName.ifEmpty { "Unknown name" }
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
                            modifier = Modifier.clickable(onClick = { }),
                            colors = ListItemDefaults.colors(
                                leadingIconColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                            ),
                            supportingContent = {
                                Text(text = remember(state) {
                                    "${state.session.city}, ${state.session.state}, ${state.session.country}"
                                })
                            }
                        )

                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                        ListItem(
                            leadingContent = {
                                Icon(imageVector = Icons.Rounded.Router, contentDescription = null)
                            },
                            headlineContent = {
                                Text(text = stringResource(id = R.string.guard_confirm_sheet_ip))
                            },
                            modifier = Modifier.clickable(onClick = { }),
                            colors = ListItemDefaults.colors(
                                leadingIconColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                            ),
                            supportingContent = {
                                Text(text = state.session.ip)
                            }
                        )

                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                        ListItem(
                            leadingContent = {
                                Icon(imageVector = Icons.Rounded.Save, contentDescription = null)
                            },
                            headlineContent = {
                                Text(text = stringResource(id = R.string.guard_confirm_sheet_remember))
                            },
                            modifier = Modifier.clickable(onClick = {
                                viewModel.rememberPassword = !viewModel.rememberPassword
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
                                    checked = viewModel.rememberPassword,
                                    onCheckedChange = { viewModel.rememberPassword = it })
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(Modifier.padding(horizontal = 16.dp)) {
                    Button(
                        onClick = { viewModel.dispatchOperation(false, onFinish) },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        colors = ButtonDefaults.filledTonalButtonColors(),
                        shape = MaterialTheme.shapes.large
                    ) {
                        if (viewModel.isDenying) {
                            ResizableCircularIndicator(indicatorSize = 19.dp, color = MaterialTheme.colorScheme.onSecondaryContainer, strokeWidth = 2.dp)
                        } else {
                            Text(text = stringResource(id = R.string.guard_confirm_sheet_action_deny), color = MaterialTheme.colorScheme.onSecondaryContainer)
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = { viewModel.dispatchOperation(true, onFinish) },
                        modifier = Modifier.weight(1f),
                        contentPadding = PaddingValues(16.dp),
                        shape = MaterialTheme.shapes.large
                    ) {
                        if (viewModel.isApproving) {
                            ResizableCircularIndicator(indicatorSize = 19.dp, color = MaterialTheme.colorScheme.onPrimary, strokeWidth = 2.dp)
                        } else {
                            Text(text = stringResource(id = R.string.guard_confirm_sheet_action_approve), color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@HiltViewModel
internal class GuardConfirmSessionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val steamClient: HostSteamClient,
) : ViewModel() {
    var state by mutableStateOf<State>(State.Loading)
        private set

    var rememberPassword by mutableStateOf(true)

    var isApproving by mutableStateOf(false)
    var isDenying by mutableStateOf(false)

    val steamId = savedStateHandle.getSteamId()
    val clientId = savedStateHandle.get<Long>(GuardMicroapp.Arguments.ClientId.name)!!

    val guardInstance = steamClient.client.guard.instanceFor(steamId) ?: error("Guard is not installed!")

    init {
        viewModelScope.launch {
            loadInfo()
        }
    }

    private suspend fun loadInfo() {
        state = State.Ready(steamClient.client.guardManagement.getActiveSessionInfo(clientId)!!.also {
            rememberPassword = it.requestedPersistedSession
        })
    }

    fun dispatchOperation(allow: Boolean, onFinish: (NavigationResult) -> Unit) {
        if (isApproving || isDenying) return

        setDynState(allow, true)

        viewModelScope.launch {
            steamClient.client.guardManagement.confirmNewSession(
                session = (state as State.Ready).session,
                approve = allow,
                persist = rememberPassword
            )

            setDynState(allow, false)
            onFinish(ConfirmedNewSession(id = (state as State.Ready).session.id, allowed = allow))
        }
    }

    private fun setDynState(allow: Boolean, state: Boolean) {
        if (allow) {
            isApproving = state
        } else {
            isDenying = state
        }
    }

    sealed class State {
        class Ready(val session: AwaitingSession) : State()
        object Loading : State()
    }
}
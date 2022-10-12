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
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetHandle
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetHeader
import bruhcollective.itaysonlab.jetisteam.uikit.components.BottomSheetSubtitle
import bruhcollective.itaysonlab.jetisteam.uikit.components.ResizableCircularIndicator
import bruhcollective.itaysonlab.jetisteam.usecases.twofactor.GetFutureAuthSession
import bruhcollective.itaysonlab.jetisteam.usecases.twofactor.UpdateSessionWithMobileAuth
import bruhcollective.itaysonlab.microapp.guard.GuardMicroappImpl
import bruhcollective.itaysonlab.microapp.guard.R
import bruhcollective.itaysonlab.microapp.guard.core.GuardController
import bruhcollective.itaysonlab.microapp.guard.utils.SessionFormatter
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okio.ByteString
import okio.ByteString.Companion.toByteString
import okio.buffer
import okio.sink
import okio.use
import steam.auth.CAuthentication_GetAuthSessionInfo_Response
import steam.auth.ESessionPersistence
import java.io.ByteArrayOutputStream
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GuardConfirmSessionSheet(
    viewModel: GuardConfirmSessionViewModel = hiltViewModel(),
    onFinish: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        BottomSheetHandle(modifier = Modifier.align(Alignment.CenterHorizontally))

        BottomSheetHeader(
            text = stringResource(id = R.string.guard_confirm_sheet_header),
            modifier = Modifier.padding(bottom = 4.dp)
        )

        BottomSheetSubtitle(text = buildAnnotatedString {
            append(stringResource(id = R.string.guard_as))
            append(" ")
            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) {
                append(viewModel.guardInstance.username)
            }
        }, modifier = Modifier.padding(bottom = 16.dp))

        when (val state = viewModel.state) {
            GuardConfirmSessionViewModel.State.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }

            is GuardConfirmSessionViewModel.State.Ready -> {
                Card(Modifier.padding(horizontal = 16.dp), shape = MaterialTheme.shapes.large) {
                    Column {
                        val visuals = remember(state.sessionInfo) {
                            SessionFormatter.formatAuthSession(state.sessionInfo)
                        }

                        ListItem(
                            leadingContent = {
                                Icon(
                                    imageVector = visuals.icon(),
                                    contentDescription = null
                                )
                            },
                            headlineText = {
                                Text(text = visuals.fallbackName)
                            },
                            modifier = Modifier.clickable(onClick = { }),
                            colors = ListItemDefaults.colors(
                                leadingIconColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                            ),
                            supportingText = {
                                Text(text = remember(state.sessionInfo) {
                                    state.sessionInfo.device_friendly_name.orEmpty().ifEmpty { "Unknown name" }
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
                            headlineText = {
                                Text(text = stringResource(id = R.string.guard_confirm_sheet_location))
                            },
                            modifier = Modifier.clickable(onClick = { }),
                            colors = ListItemDefaults.colors(
                                leadingIconColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                            ),
                            supportingText = {
                                Text(text = remember(state) {
                                    "${state.sessionInfo.city}, ${state.sessionInfo.state}, ${state.sessionInfo.country}"
                                })
                            }
                        )

                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                        ListItem(
                            leadingContent = {
                                Icon(imageVector = Icons.Rounded.Router, contentDescription = null)
                            },
                            headlineText = {
                                Text(text = stringResource(id = R.string.guard_confirm_sheet_ip))
                            },
                            modifier = Modifier.clickable(onClick = { }),
                            colors = ListItemDefaults.colors(
                                leadingIconColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                            ),
                            supportingText = {
                                Text(text = state.sessionInfo.ip.orEmpty())
                            }
                        )

                        Divider(color = MaterialTheme.colorScheme.surfaceVariant)

                        ListItem(
                            leadingContent = {
                                Icon(imageVector = Icons.Rounded.Save, contentDescription = null)
                            },
                            headlineText = {
                                Text(text = stringResource(id = R.string.guard_confirm_sheet_remember))
                            },
                            modifier = Modifier.clickable(onClick = {
                                viewModel.rememberPassword = !viewModel.rememberPassword
                            }),
                            colors = ListItemDefaults.colors(
                                leadingIconColor = MaterialTheme.colorScheme.primary,
                                containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                            ),
                            supportingText = {
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

                    Spacer(modifier = Modifier.width(8.dp))

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
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}

@HiltViewModel
internal class GuardConfirmSessionViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    guardController: GuardController,
    private val getFutureAuthSession: GetFutureAuthSession,
    private val updateSessionWithMobileAuth: UpdateSessionWithMobileAuth
) : ViewModel() {
    var state by mutableStateOf<State>(State.Loading)
        private set

    var rememberPassword by mutableStateOf(true)

    var isApproving by mutableStateOf(false)
    var isDenying by mutableStateOf(false)

    val steamId = SteamID(savedStateHandle.get<String>(GuardMicroappImpl.InternalRoutes.ARG_STEAM_ID)!!.toLong())
    val clientId = savedStateHandle.get<String>(GuardMicroappImpl.InternalRoutes.ARG_CLIENT_ID)!!.toLong()

    val guardInstance = guardController.getInstance(steamId) ?: error("Guard is not installed!")

    init {
        viewModelScope.launch {
            loadInfo()
        }
    }

    private suspend fun loadInfo() {
        state = State.Ready(getFutureAuthSession(clientId).also {
            rememberPassword = it.requested_persistence == ESessionPersistence.k_ESessionPersistence_Persistent
        })
    }

    fun dispatchOperation(allow: Boolean, onFinish: () -> Unit) {
        if (isApproving || isDenying) return

        setDynState(allow, true)

        viewModelScope.launch {
            val version = (state as State.Ready).sessionInfo.version ?: 1

            updateSessionWithMobileAuth(
                version = version,
                clientId = clientId,
                steamId = steamId.steamId,
                allow = allow,
                signature = generateSignature(version),
                persistence = if (rememberPassword) {
                    ESessionPersistence.k_ESessionPersistence_Persistent
                } else {
                    ESessionPersistence.k_ESessionPersistence_Ephemeral
                }
            )

            setDynState(allow, false)
            onFinish()
        }
    }

    private fun generateSignature(version: Int): ByteString {
        return ByteArrayOutputStream().apply {
            sink().buffer().use { sink ->
                sink.writeShortLe(version)
                sink.writeLongLe(clientId)
                sink.writeLongLe(steamId.steamId)
            }
        }.toByteArray().let {
            guardInstance.digestSha256(it)
        }.toByteString()
    }

    private fun setDynState(allow: Boolean, state: Boolean) {
        if (allow) {
            isApproving = state
        } else {
            isDenying = state
        }
    }

    sealed class State {
        class Ready(val sessionInfo: CAuthentication_GetAuthSessionInfo_Response) : State()
        object Loading : State()
    }
}
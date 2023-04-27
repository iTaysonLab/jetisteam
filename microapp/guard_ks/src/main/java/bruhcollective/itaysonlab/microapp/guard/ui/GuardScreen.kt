package bruhcollective.itaysonlab.microapp.guard.ui

import android.os.Build
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import bruhcollective.itaysonlab.jetisteam.uikit.components.IndicatorBehindScrollableTabRow
import bruhcollective.itaysonlab.jetisteam.uikit.components.tabIndicatorOffset
import bruhcollective.itaysonlab.ksteam.guard.models.CodeModel
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.results.InstallResultHandler
import bruhcollective.itaysonlab.microapp.guard.R
import bruhcollective.itaysonlab.microapp.guard.ui.variants.NoGuardScreen
import bruhcollective.itaysonlab.microapp.guard.ui.variants.pages.GuardCodeAndConfirmationsPage
import bruhcollective.itaysonlab.microapp.guard.ui.variants.pages.GuardSessionsPage
import bruhcollective.itaysonlab.microapp.guard.utils.ConfirmationDetailResult
import bruhcollective.itaysonlab.microapp.guard.utils.ConfirmedNewSession
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
internal fun GuardScreen(
    viewModel: GuardViewModel = hiltViewModel(),
    backStack: NavBackStackEntry,
    onAddClicked: (Long) -> Unit,
    onDeleteClicked: (Long) -> Unit,
    onQrClicked: (Long) -> Unit,
    onRecoveryClicked: (Long) -> Unit,
    onConfirmationClicked: (Long, String) -> Unit,
    onSessionClicked: (Long, GuardViewModel.ActiveSessionWrapper) -> Unit,
    onSessionArrived: (Long, Long) -> Unit,
) {
    val isConnectedToSteam = viewModel.connectedToSteam.collectAsStateWithLifecycle(initialValue = false)

    val clipboard = LocalClipboardManager.current
    val context = LocalContext.current

    val scope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }

    when (val state = viewModel.state) {
        is GuardViewModel.GuardState.Available -> {
            val pagerState = rememberPagerState()

            val codeState =
                state.instance.code.collectAsStateWithLifecycle(initialValue = CodeModel.DefaultInstance, context = Dispatchers.IO)

            val awaitingSession =
                viewModel.awaitingSessionPoll.collectAsStateWithLifecycle(initialValue = null, context = Dispatchers.IO)

            InstallResultHandler(backStack) { sessionEvent ->
                if (sessionEvent is ConfirmedNewSession) {
                    delay(1000L)
                    viewModel.reloadSessions()
                } else if (sessionEvent is ConfirmationDetailResult) {
                    delay(1000L)
                    viewModel.reloadConfirmations()
                }
            }

            LaunchedEffect(awaitingSession.value) {
                awaitingSession.value?.let { id ->
                    if (id != 0L) {
                        onSessionArrived(viewModel.steamId, id)
                    }
                }
            }

            Scaffold(topBar = {
                IndicatorBehindScrollableTabRow(
                    selectedTabIndex = pagerState.currentPage,
                    containerColor = Color.Transparent,
                    indicator = { tabPositions ->
                        Box(
                            Modifier
                                .padding(vertical = 12.dp)
                                .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                                .fillMaxHeight()
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.onSurface)
                        )
                    },
                    edgePadding = 16.dp,
                    modifier = Modifier.statusBarsPadding(),
                    tabAlignment = Alignment.Center
                ) {
                    Tab(
                        selected = pagerState.currentPage == 0,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        },
                        selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ) {
                        Text(
                            text = stringResource(id = R.string.guard).uppercase(Locale.getDefault()),
                            modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }

                    Tab(
                        selected = pagerState.currentPage == 1,
                        onClick = {
                            scope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        },
                        selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                        unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    ) {
                        Text(
                            text = stringResource(id = R.string.guard_sessions).uppercase(Locale.getDefault()),
                            modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp
                        )
                    }
                }
            }, contentWindowInsets = EmptyWindowInsets, snackbarHost = {
                SnackbarHost(hostState = snackState) {
                    Snackbar(snackbarData = it)
                }
            }) { innerPadding ->
                HorizontalPager(
                    pageCount = 2,
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) { pos ->
                    if (pos == 0) {
                        GuardCodeAndConfirmationsPage(
                            modifier = Modifier.fillMaxSize(),
                            code = codeState.value,
                            connectedToSteam = isConnectedToSteam.value,
                            confirmationState = viewModel.confirmations,
                            onSignWithQrClicked = { onQrClicked(viewModel.steamId) },
                            onDeleteClicked = { onDeleteClicked(viewModel.steamId) },
                            onRecoveryClicked = { onRecoveryClicked(viewModel.steamId) },
                            onCopyClicked = {
                                scope.launch {
                                    clipboard.setText(AnnotatedString(codeState.value.code))

                                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
                                        snackState.showSnackbar(message = context.getString(R.string.guard_actions_copy_snack))
                                    }
                                }
                            }, onConfirmationClicked = { confirmation ->
                                onConfirmationClicked(viewModel.steamId, viewModel.wrapConfirmation(confirmation))
                            })
                    } else if (pos == 1) {
                        GuardSessionsPage(modifier = Modifier.fillMaxSize(), sessions = viewModel.sessions, onSessionClicked = { session ->
                            onSessionClicked(viewModel.steamId, session)
                        })
                    }
                }
            }
        }

        GuardViewModel.GuardState.Setup -> {
            NoGuardScreen(
                onAddClicked = { onAddClicked(viewModel.steamId) },
                modifier = Modifier.fillMaxSize(),
                connectedToSteam = isConnectedToSteam.value
            )
        }
    }
}
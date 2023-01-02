package bruhcollective.itaysonlab.microapp.library.ui.remote

import android.text.format.Formatter
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.ExperimentalLifecycleComposeApi
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.components.StateTextButton
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenLoading
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.library.R
import bruhcollective.itaysonlab.microapp.library.ui.remote.pages.LibraryPage
import bruhcollective.itaysonlab.microapp.library.ui.remote.pages.QueuePage
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class, ExperimentalLifecycleComposeApi::class)
@Composable
fun RemoteMachineScreen(
    onBackClick: () -> Unit,
    onVisitStoreClicked: (Int) -> Unit,
    viewModel: RemoteMachineViewModel = hiltViewModel()
) {
    val pagerState = rememberPagerState()
    val cScope = rememberCoroutineScope()

    var uninstallAlert by remember {
        mutableStateOf<Pair<Int, String>?>(null)
    }

    if (uninstallAlert != null) {
        AlertDialog(
            onDismissRequest = {
                if (viewModel.isUninstalling.not()) {
                    uninstallAlert = null
                }
            },
            icon = {
                Icon(Icons.Rounded.Delete, contentDescription = null)
            }, title = {
                Text(text = stringResource(id = R.string.library_remote_uninstall))
            }, text = {
                Text(text = stringResource(id = R.string.library_remote_uninstall_text, uninstallAlert?.second.orEmpty()))
            }, confirmButton = {
                StateTextButton(onClick = { viewModel.uninstallGame(uninstallAlert?.first ?: 0) {
                    uninstallAlert = null
                }}, inLoadingState = viewModel.isUninstalling) {
                    Text(text = stringResource(id = R.string.library_remote_uninstall_action))
                }
            }, dismissButton = {
                TextButton(onClick = { uninstallAlert = null }) {
                    Text(text = stringResource(id = R.string.library_remote_uninstall_cancel))
                }
            }
        )
    }

    PageLayout(state = viewModel.state, onReload = viewModel::reload) { data ->
        val machineState = viewModel.machineStateFlow.collectAsStateWithLifecycle(initialValue = null)
        val machineStateValue = machineState.value

        if (machineStateValue != null) {
            Scaffold(topBar = {
                TopAppBar(
                    title = {
                        Column {
                            val ctx = LocalContext.current

                            val formattedBytes = remember(machineStateValue.formattedBytesAvailable) {
                                Formatter.formatFileSize(ctx, machineStateValue.formattedBytesAvailable)
                            }

                            Text(
                                text = machineStateValue.info.machine_name.orEmpty(),
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 16.sp
                            )

                            Text(
                                text = stringResource(
                                    id = R.string.library_remote_subtitle,
                                    machineStateValue.info.os.orEmpty(),
                                    formattedBytes
                                ), color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 13.sp
                            )
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }, contentWindowInsets = EmptyWindowInsets) { innerPadding ->
                Column(Modifier.padding(innerPadding)) {
                    ScrollableTabRow(selectedTabIndex = 0, divider = {},
                        edgePadding = 24.dp,
                        indicator = { tabPositions ->
                            TabRowDefaults.Indicator(
                                Modifier
                                    .pagerTabIndicatorOffset(
                                        pagerState,
                                        tabPositions
                                    )
                                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                            )
                        }) {

                        Tab(selected = pagerState.currentPage == 0, onClick = {
                            cScope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                        }, text = {
                            Text(text = stringResource(id = R.string.library_remote_tab_queue))
                        })

                        Tab(selected = pagerState.currentPage == 1, onClick = {
                            cScope.launch {
                                pagerState.animateScrollToPage(1)
                            }
                        }, text = {
                            Text(text = stringResource(id = R.string.library_remote_tab_installed))
                        })
                    }

                    RoundedPage(modifier = Modifier.fillMaxSize()) {
                        HorizontalPager(count = 2, state = pagerState) { page ->
                            if (page == 0) {
                                QueuePage(machineStateValue, viewModel.currentJobId, viewModel::handleClick)
                            } else {
                                LibraryPage(data.installed) { appId, appName, action ->
                                    if (action == 0) {
                                        onVisitStoreClicked(appId)
                                    } else if (action == 1) {
                                        uninstallAlert = appId to appName
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            FullscreenLoading()
        }
    }
}

@ExperimentalPagerApi
private fun Modifier.pagerTabIndicatorOffset(
    pagerState: PagerState,
    tabPositions: List<TabPosition>,
    pageIndexMapping: (Int) -> Int = { it },
): Modifier = layout { measurable, constraints ->
    if (tabPositions.isEmpty()) {
        // If there are no pages, nothing to show
        layout(constraints.maxWidth, 0) {}
    } else {
        val currentPage = minOf(tabPositions.lastIndex, pageIndexMapping(pagerState.currentPage))
        val currentTab = tabPositions[currentPage]
        val previousTab = tabPositions.getOrNull(currentPage - 1)
        val nextTab = tabPositions.getOrNull(currentPage + 1)
        val fraction = pagerState.currentPageOffset
        val indicatorWidth = if (fraction > 0 && nextTab != null) {
            lerp(currentTab.width, nextTab.width, fraction).roundToPx()
        } else if (fraction < 0 && previousTab != null) {
            lerp(currentTab.width, previousTab.width, -fraction).roundToPx()
        } else {
            currentTab.width.roundToPx()
        }
        val indicatorOffset = if (fraction > 0 && nextTab != null) {
            lerp(currentTab.left, nextTab.left, fraction).roundToPx()
        } else if (fraction < 0 && previousTab != null) {
            lerp(currentTab.left, previousTab.left, -fraction).roundToPx()
        } else {
            currentTab.left.roundToPx()
        }
        val placeable = measurable.measure(
            Constraints(
                minWidth = indicatorWidth,
                maxWidth = indicatorWidth,
                minHeight = 0,
                maxHeight = constraints.maxHeight
            )
        )
        layout(constraints.maxWidth, maxOf(placeable.height, constraints.minHeight)) {
            placeable.place(
                indicatorOffset,
                maxOf(constraints.minHeight - placeable.height, 0)
            )
        }
    }
}
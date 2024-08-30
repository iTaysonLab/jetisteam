package bruhcollective.itaysonlab.cobalt.guard.instance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.QrCodeScanner
import androidx.compose.material3.Badge
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.cobalt.ui.components.IndicatorBehindScrollableTabRow
import bruhcollective.itaysonlab.cobalt.ui.components.tabIndicatorOffset
import kotlinx.coroutines.launch
import java.util.Locale

@Composable
fun GuardInstanceScreen(
    component: GuardInstanceComponent,
) {
    val state by component.state.collectAsState()

    val pagerState = rememberPagerState { 3 }
    val guardSessionListState = rememberLazyListState()
    val clipboardManager = LocalClipboardManager.current

    Scaffold(topBar = {
        GuardInstanceHeader(
            pagerState = pagerState,
            modifier = Modifier.statusBarsPadding()
        )
    }, floatingActionButton = {
        FloatingActionButton(onClick = component::openQrScanner) {
            Icon(
                imageVector = Icons.Rounded.QrCodeScanner,
                contentDescription = stringResource(id = R.string.guard_qr_action)
            )
        }
    }, contentWindowInsets = EmptyWindowInsets) { innerPadding ->
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { pos ->
            when (pos) {
                0 -> GuardCodePage(
                    code = state.code,
                    codeProgress = state.codeProgress,
                    onCopyClicked = {
                        // TODO: Lower Androids without a toast
                        clipboardManager.setText(AnnotatedString(state.code))
                    },
                    onDeleteClicked = component::openDeleteSheet,
                    onRecoveryClicked = component::openRecoveryCodeSheet,
                    modifier = Modifier.fillMaxSize()
                )

                1 -> GuardConfirmationsPage(
                    confirmationState = state.confirmations,
                    modifier = Modifier.fillMaxSize(),
                    onRefresh = component::reloadConfirmations,
                    onConfirmationClicked = component::openConfirmationDetail,
                    isRefreshing = state.areConfirmationsLoading
                )

                2 -> GuardSessionsPage(
                    listState = guardSessionListState,
                    isRefreshing = state.areSessionsLoading,
                    currentSession = state.currentSession,
                    sessions = state.sessions,
                    onSessionClicked = component::openSessionDetail,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}

@Composable
private fun GuardInstanceHeader(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    confirmationCount: Int = 0
) {
    val scope = rememberCoroutineScope()

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
        modifier = modifier,
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
                text = stringResource(id = R.string.guard_code).uppercase(Locale.getDefault()),
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
            if (confirmationCount > 0) {
                val contentColor = LocalContentColor.current

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.guard_confirms).uppercase(Locale.getDefault()),
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )

                    Badge(
                        containerColor = LocalContentColor.current,
                        contentColor = Color(
                            red = contentColor.colorSpace.getMaxValue(0) - contentColor.red,
                            green = contentColor.colorSpace.getMaxValue(1) - contentColor.green,
                            blue = contentColor.colorSpace.getMaxValue(2) - contentColor.blue
                        )
                    ) {
                        Text(text = confirmationCount.toString())
                    }
                }
            } else {
                Text(
                    text = stringResource(id = R.string.guard_confirms).uppercase(Locale.getDefault()),
                    fontWeight = FontWeight.Medium,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp)
                )
            }
        }

        Tab(
            selected = pagerState.currentPage == 2,
            onClick = {
                scope.launch {
                    pagerState.animateScrollToPage(2)
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
}
package bruhcollective.itaysonlab.microapp.notifications.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.floatingWindowInsetsAsPaddings
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenLoading
import bruhcollective.itaysonlab.ksteam.models.notifications.Notification
import bruhcollective.itaysonlab.ksteam.models.notifications.NotificationFeed
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.notifications.R

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
internal fun NotificationsScreen(
    viewModel: NotificationsScreenViewModel = hiltViewModel(),
    onClick: (Notification) -> Unit
) {
    val tas = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val swipeRefreshState =
        rememberPullRefreshState(viewModel.isRefreshing, onRefresh = viewModel::swipeRefreshReload)

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.notifications))
                }, scrollBehavior = tas, colors = TopAppBarDefaults.topAppBarColors(
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        }, modifier = Modifier
            .nestedScroll(tas.nestedScrollConnection)
            .fillMaxSize(), contentWindowInsets = EmptyWindowInsets
    ) { innerPadding ->
        RoundedPage(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(swipeRefreshState)
                .padding(innerPadding)
        ) {
            when (val feed = viewModel.feed.collectAsStateWithLifecycle().value) {
                is NotificationFeed.Loaded -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = floatingWindowInsetsAsPaddings(16.dp)
                    ) {
                        items(feed.notifications) { notification ->
                            NotificationRenderer(notification) { onClick(notification) }
                        }
                    }
                }

                else -> {
                    FullscreenLoading()
                }
            }

            PullRefreshIndicator(viewModel.isRefreshing, swipeRefreshState, Modifier.align(Alignment.TopCenter))
        }
    }
}
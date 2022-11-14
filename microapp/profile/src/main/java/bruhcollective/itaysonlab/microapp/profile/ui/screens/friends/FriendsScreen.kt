package bruhcollective.itaysonlab.microapp.profile.ui.screens.friends

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.profile.R

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class, ExperimentalMaterialApi::class,
)
@Composable
internal fun FriendsScreen(
    onFriendClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    viewModel: FriendsScreenViewModel = hiltViewModel()
) {
    val tas = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    val listState = rememberLazyListState()
    val isRefreshing = viewModel.swipeRefreshLoading.collectAsState()

    val isRefreshingState =
        rememberPullRefreshState(isRefreshing.value, onRefresh = viewModel::swipeRefreshReload)

    PageLayout(state = viewModel.state, onReload = viewModel::reload) { data ->
        Scaffold(
            topBar = {
                LargeTopAppBar(title = {
                    Text(text = stringResource(id = R.string.friends))
                }, scrollBehavior = tas, navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                })
            }, modifier = Modifier
                .nestedScroll(tas.nestedScrollConnection)
                .fillMaxSize(), contentWindowInsets = EmptyWindowInsets
        ) { innerPadding ->
            RoundedPage(
                modifier = Modifier
                    .padding(innerPadding)
                    .pullRefresh(isRefreshingState)
                    .fillMaxSize()
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    data.forEach { (group, list) ->
                        if (list.isNotEmpty()) {
                            stickyHeader {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
                                        .padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = stringResource(id = group.stringRes),
                                        fontWeight = FontWeight.Bold,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.padding(horizontal = 20.dp)
                                    )
                                }
                            }

                            items(list) { friend ->
                                FriendListItem(group, friend, onFriendClick)
                            }
                        }
                    }
                }

                PullRefreshIndicator(isRefreshing.value, isRefreshingState, Modifier.align(Alignment.TopCenter))
            }
        }
    }
}

enum class FriendGroups(val stringRes: Int, val color: Color) {
    OFFLINE(R.string.friends_group_offline, Color(0xFF898989)),
    ONLINE(R.string.friends_group_online, Color(0xFF57cbde)),
    PLAYING(R.string.friends_group_playing, Color(0xFFa3cf06))
}
package bruhcollective.itaysonlab.microapp.profile.ui.screens.friends

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.microapp.profile.R
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalComposeUiApi::class,
)
@Composable
internal fun FriendsScreen(
    onFriendClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    viewModel: FriendsScreenViewModel = hiltViewModel()
) {
    val listState = rememberLazyListState()
    val isRefreshing = viewModel.swipeRefreshLoading.collectAsState()
    val isRefreshingState = rememberSwipeRefreshState(isRefreshing.value)

    PageLayout(state = viewModel.state, onReload = viewModel::reload) { data ->
        Column(modifier = Modifier.fillMaxSize()) {
            SmallTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.friends))
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
            )

            SwipeRefresh(state = isRefreshingState, onRefresh = { viewModel.swipeRefreshReload() }) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 12.dp)
                ) {
                    data.forEach { (group, list) ->
                        if (list.isNotEmpty()) {
                            stickyHeader {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(MaterialTheme.colorScheme.surface)
                                ) {
                                    Text(
                                        text = stringResource(id = group.stringRes),
                                        fontSize = 20.sp,
                                        lineHeight = 24.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(4.dp)
                                    )
                                }
                            }

                            items(list) { friend ->
                                FriendListItem(group, friend, onFriendClick)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }
        }
    }
}

enum class FriendGroups(val stringRes: Int, val color: Color) {
    OFFLINE(R.string.friends_group_offline, Color(0xFF898989)),
    ONLINE(R.string.friends_group_online, Color(0xFF57cbde)),
    PLAYING(R.string.friends_group_playing, Color(0xFFa3cf06))
}
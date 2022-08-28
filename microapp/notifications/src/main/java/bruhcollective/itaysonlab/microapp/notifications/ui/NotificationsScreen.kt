package bruhcollective.itaysonlab.microapp.notifications.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.microapp.notifications.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NotificationsScreen(
    viewModel: NotificationsScreenViewModel = hiltViewModel()
) {
    val tas = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()

    PageLayout(state = viewModel.state, onReload = viewModel::reload) { page ->
        Scaffold(
            topBar = {
                LargeTopAppBar(title = {
                    Text(text = stringResource(id = R.string.notifications))
                }, scrollBehavior = tas)
            }, modifier = Modifier
                .nestedScroll(tas.nestedScrollConnection)
                .fillMaxSize()
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(it)
            ) {
                items(page.notifications) { notification ->
                    Notification(notification)
                }
            }
        }
    }
}
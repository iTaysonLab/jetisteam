package bruhcollective.itaysonlab.microapp.gamepage.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GamePageScreen(
    viewModel: GamePageViewModel = hiltViewModel()
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state = topAppBarState)

    PageLayout(state = viewModel.state, onReload = viewModel::reload) { data ->
        Scaffold(topBar = {
            SmallTopAppBar(
                title = { data.gameName },
                scrollBehavior = scrollBehavior, colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = Color.Transparent,
                    actionIconContentColor = Color.White,
                )
            )
        }, modifier = Modifier
            .nestedScroll(scrollBehavior.nestedScrollConnection)
            .fillMaxSize()) {
            LazyColumn {
                item {
                    Text(text = data.gameName)
                    Text(text = data.gameDescription)
                }
            }
        }
    }
}
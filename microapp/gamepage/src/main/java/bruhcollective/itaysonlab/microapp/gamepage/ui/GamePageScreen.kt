package bruhcollective.itaysonlab.microapp.gamepage.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.BackHand
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.jetisteam.util.SteamBbToMarkdown
import bruhcollective.itaysonlab.microapp.gamepage.ui.components.GamePageHeader
import bruhcollective.itaysonlab.microapp.gamepage.ui.components.GamePageInfo
import bruhcollective.itaysonlab.microapp.gamepage.ui.components.GamePageScreenshots
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.material3.Material3RichText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun GamePageScreen(
    onBackClick: () -> Unit,
    viewModel: GamePageViewModel = hiltViewModel()
) {
    val topAppBarState = rememberTopAppBarState()
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(state = topAppBarState)

    PageLayout(state = viewModel.state, onReload = viewModel::reload) { data ->
        Scaffold(
            topBar = {
                SmallTopAppBar(
                    title = { data.details.name },
                    scrollBehavior = scrollBehavior,
                    colors = TopAppBarDefaults.smallTopAppBarColors(
                        containerColor = Color.Transparent,
                        actionIconContentColor = Color.White,
                    ),
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                        }
                    }
                )
            }, modifier = Modifier
                .nestedScroll(scrollBehavior.nestedScrollConnection)
                .fillMaxSize()
        ) {
            LazyColumn {
                item {
                    GamePageHeader(
                        gameTitle = data.details.name,
                        backgroundUrl = data.backgroundUrl,
                        reviews = data.reviewsInfo,
                        tags = data.tags
                    )
                }

                item {
                    GamePageScreenshots(
                        urls = data.screenshots
                    )
                }

                item {
                    GamePageInfo(
                        publishers = data.details.publishers.orEmpty(),
                        developers = data.details.developers.orEmpty(),
                        franchises = data.details.franchises.orEmpty(),
                        releaseDate = data.releaseDate
                    )
                }

                item {
                    Material3RichText(modifier = Modifier.padding(16.dp)) {
                        Markdown(content = remember(data.details.fullDescription) {
                            SteamBbToMarkdown.bbcodeToMarkdown(data.details.fullDescription)
                        })
                    }
                }
            }
        }
    }
}
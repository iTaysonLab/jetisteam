package bruhcollective.itaysonlab.microapp.library.ui.game_page.community

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Article
import androidx.compose.material.icons.rounded.Image
import androidx.compose.material.icons.rounded.PlayCircleOutline
import androidx.compose.material.icons.rounded.ScreenshotMonitor
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.floatingWindowInsetsAsPaddings
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.ksteam.models.news.community.CommunityHubPost
import bruhcollective.itaysonlab.microapp.library.ui.game_page.community.cards.CommunitySmallGridCard
import coil.compose.AsyncImage

@Composable
internal fun GamepageCommunityScreen(
    viewModel: GamepageCommunityViewModel = hiltViewModel()
) {
    PageLayout(viewModel.state) { list ->
        LazyVerticalGrid(
            modifier = Modifier.fillMaxSize(),
            contentPadding = floatingWindowInsetsAsPaddings(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            columns = GridCells.Fixed(2) // TODO: tablets
        ) {
            itemsIndexed(list, key = { index, item ->
                item.fileId
            }) { index, item ->
                CommunitySmallGridCard(
                    background = {
                        AsyncImage(
                            model = item.previewImage,
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }, icon = {
                        Icon(
                            imageVector = when (item) {
                                is CommunityHubPost.Artwork -> Icons.Rounded.Image
                                is CommunityHubPost.CommunityItem -> Icons.Rounded.Article
                                is CommunityHubPost.Screenshot -> Icons.Rounded.ScreenshotMonitor
                                is CommunityHubPost.Video -> Icons.Rounded.PlayCircleOutline
                            }, contentDescription = null
                        )
                    }, modifier = Modifier.aspectRatio(1f)
                )
            }
        }
    }
}
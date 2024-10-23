package bruhcollective.itaysonlab.cobalt.library.screenshots

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.compose.nonNullableAnimatedVisibilityScope
import bruhcollective.itaysonlab.cobalt.compose.nonNullableNavSharedTransitionScope
import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.cobalt.ui.components.RoundedPage
import coil.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ScreenshotsScreen(
    component: ScreenshotsComponent
) {
    val gridState = rememberLazyGridState()
    val scrollToTopFlag by component.scrollToTopFlag.subscribeAsState()

    LaunchedEffect(scrollToTopFlag) {
        if (scrollToTopFlag) {
            gridState.animateScrollToItem(0)
            component.resetScrollToTop()
        }
    }

    val screenResult by component.screenResult.subscribeAsState()
    val pickedApplication by component.selectedApplication.subscribeAsState()
    val actualScreenshots by component.screenshots.subscribeAsState()
    val isRefreshing by component.isRefreshing.subscribeAsState()

    RoundedPage(modifier = Modifier.fillMaxSize()) {
        when (screenResult) {
            CobaltScreenResult.Loading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            CobaltScreenResult.Loaded -> {
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = component::refresh
                ) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        state = gridState
                    ) {
                        items(actualScreenshots) { screenshot ->
                            //with(nonNullableNavSharedTransitionScope()) {
                                AsyncImage(
                                    model = screenshot.info.previewUrl,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .clip(MaterialTheme.shapes.medium)
                                        .background(Color.Black)
                                        .fillMaxWidth()
                                        .aspectRatio(1f)
                                        //.sharedElement(
                                        //    state = rememberSharedContentState(key = screenshot.id),
                                        //    animatedVisibilityScope = nonNullableAnimatedVisibilityScope()
                                        //)
                                        .clickable { component.onScreenshotClicked(screenshot) },
                                    contentScale = ContentScale.Fit
                                )
                            //}
                        }
                    }
                }
            }

            else -> {

            }
        }
    }
}
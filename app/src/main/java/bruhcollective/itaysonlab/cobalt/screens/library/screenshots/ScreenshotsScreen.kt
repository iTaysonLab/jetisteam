package bruhcollective.itaysonlab.cobalt.screens.library.screenshots

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.cobalt.library.screenshots.ScreenshotsComponent
import bruhcollective.itaysonlab.cobalt.screens.library.screenshots.alert.ModalSelectApplicationSheet
import bruhcollective.itaysonlab.cobalt.screens.library.screenshots.alert.ModalSelectScreenshotFilterSheet
import bruhcollective.itaysonlab.cobalt.screens.published_files.ModalPublishedFullscreenPhotoViewer
import bruhcollective.itaysonlab.cobalt.ui.ScrollToTopHandler
import bruhcollective.itaysonlab.cobalt.ui.components.ExceptionPage
import bruhcollective.itaysonlab.cobalt.ui.components.RoundedPage
import coil.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch

@OptIn(
    ExperimentalSharedTransitionApi::class, ExperimentalMaterial3Api::class,
    ExperimentalMaterial3ExpressiveApi::class
)
@Composable
fun ScreenshotsScreen(
    isFocused: Boolean,
    component: ScreenshotsComponent
) {
    val gridState = rememberLazyGridState()
    val scope = rememberCoroutineScope()

    ScrollToTopHandler(isFocused) {
        scope.launch {
            gridState.animateScrollToItem(0)
        }
    }

    val alertState by component.alertChild.subscribeAsState()
    val screenResult by component.screenResult.subscribeAsState()
    val pickedApplication by component.selectedApplication.subscribeAsState()
    val actualScreenshots by component.screenshots.subscribeAsState()
    val isRefreshing by component.isRefreshing.subscribeAsState()
    val isPagingAvailable by component.isPagingAvailable.subscribeAsState()

    alertState.child?.instance?.let { child ->
        when (child) {
            is ScreenshotsComponent.AlertChild.PhotoViewer -> {
                ModalPublishedFullscreenPhotoViewer(
                    onDismiss = component::dismissAlert,
                    component = child.component
                )
            }

            is ScreenshotsComponent.AlertChild.PickApplication -> {
                ModalSelectApplicationSheet(
                    onDismiss = component::dismissAlert,
                    component = child.component
                )
            }

            is ScreenshotsComponent.AlertChild.SetParameters -> {
                ModalSelectScreenshotFilterSheet(
                    onDismiss = component::dismissAlert,
                    component = child.component
                )
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            val app = pickedApplication
            val selected = app.id != 0

            FilterChip(
                selected = selected,
                onClick = component::onApplicationFilterClicked,
                label = {
                    if (app.name.isNotEmpty()) {
                        Text(app.name)
                    } else {
                        Text(stringResource(R.string.library_games_chip_collections_default))
                    }
                },
                trailingIcon = {
                    Icon(Icons.Rounded.ArrowDropDown, contentDescription = null)
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp),
                    // selectedContainerColor = chipSelectedBackground
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = selected,
                    borderColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )

            Spacer(Modifier.weight(1f))

            IconButton(onClick = component::onFilterClicked) {
                Icon(
                    Icons.Rounded.Tune,
                    contentDescription = stringResource(R.string.library_screenshots_filter_sheet)
                )
            }
        }

        RoundedPage(modifier = Modifier.fillMaxSize()) {
            when (val s = screenResult) {
                CobaltScreenResult.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        LoadingIndicator()
                    }
                }

                is CobaltScreenResult.Error -> {
                    ExceptionPage(
                        result = s,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }

                CobaltScreenResult.Loaded -> {
                    PullToRefreshBox(
                        isRefreshing = isRefreshing,
                        onRefresh = component::refresh
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
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
                                        .aspectRatio(16f / 10f)
                                        //.sharedElement(
                                        //    state = rememberSharedContentState(key = screenshot.id),
                                        //    animatedVisibilityScope = nonNullableAnimatedVisibilityScope()
                                        //)
                                        .clickable { component.onScreenshotClicked(screenshot) },
                                    contentScale = ContentScale.Fit
                                )
                                //}
                            }

                            if (isPagingAvailable) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    LaunchedEffect(Unit) {
                                        component.onPagingRequested()
                                    }

                                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                        LoadingIndicator()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
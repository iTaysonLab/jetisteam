package bruhcollective.itaysonlab.cobalt.screens.library.games

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
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.ElevatedFilterChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearWavyProgressIndicator
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.cobalt.library.games.GamesComponent
import bruhcollective.itaysonlab.cobalt.screens.library.games.alert.ModalGameSheet
import bruhcollective.itaysonlab.cobalt.screens.library.games.alert.ModalSelectCollectionSheet
import bruhcollective.itaysonlab.cobalt.ui.ScrollToTopHandler
import bruhcollective.itaysonlab.cobalt.ui.components.ExceptionPage
import bruhcollective.itaysonlab.cobalt.ui.components.RoundedPage
import bruhcollective.itaysonlab.cobalt.ui.theme.partialShapes
import bruhcollective.itaysonlab.ksteam.models.app.OwnedSteamApplication
import bruhcollective.itaysonlab.ksteam.models.enums.ELanguage
import coil.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun GamesScreen(isFocused: Boolean, component: GamesComponent) {
    val scope = rememberCoroutineScope()
    val gridState = rememberLazyGridState()

    ScrollToTopHandler(isFocused) {
        scope.launch {
            gridState.animateScrollToItem(0)
        }
    }

    val alertState by component.alertState.subscribeAsState()
    val screenResult by component.screenResult.subscribeAsState()

    val picsState by component.picsState.subscribeAsState()
    val picsProgress by component.picsProgress.subscribeAsState()

    val currentGames by component.games.subscribeAsState()
    val currentCollectionName by component.currentCollectionName.subscribeAsState()
    val wasDefaultQueryModified by component.wasDefaultQueryModified.subscribeAsState()
    val canLoadMore by component.canLoadMore.subscribeAsState()

    alertState.child?.instance?.let { child ->
        when (child) {
            is GamesComponent.AlertChild.SelectCollection -> {
                ModalSelectCollectionSheet(onDismiss = component::dismissAlert, component = child.component)
            }

            is GamesComponent.AlertChild.GameSheet -> {
                ModalGameSheet(onDismiss = component::dismissAlert, component = child.component)
            }

            is GamesComponent.AlertChild.EditCollection -> TODO()
            is GamesComponent.AlertChild.SelectSort -> TODO()
        }
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilterChip(
                selected = wasDefaultQueryModified,
                onClick = component::onCollectionTileClicked,
                label = {
                    if (currentCollectionName.isNotEmpty()) {
                        Text(currentCollectionName)
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
                    selected = wasDefaultQueryModified,
                    borderColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            )

            Spacer(Modifier.weight(1f))

            IconButton(onClick = component::onFilterTileClicked) {
                Icon(
                    Icons.Rounded.Tune,
                    contentDescription = stringResource(R.string.library_games_chip_filter)
                )
            }

            IconButton(onClick = component::onSortTileClicked) {
                Icon(
                    Icons.AutoMirrored.Rounded.Sort,
                    contentDescription = stringResource(R.string.library_games_chip_sort)
                )
            }
        }

        RoundedPage(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            if (picsState) {
                when (val s = screenResult) {
                    CobaltScreenResult.Loading -> {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingIndicator()
                        }
                    }

                    is CobaltScreenResult.Error -> {
                        ExceptionPage(
                            result = s,
                            modifier = Modifier.fillMaxSize().padding(16.dp)
                        )
                    }

                    CobaltScreenResult.Loaded -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            state = gridState
                        ) {
                            itemsIndexed(currentGames, key = { index, item -> item.application.id.value }) { index, app ->
                                val shape = when (index) {
                                    0 -> MaterialTheme.partialShapes.largeTopLeftShape
                                    2 -> MaterialTheme.partialShapes.largeTopRightShape
                                    else -> RectangleShape
                                }

                                Box(
                                    modifier = Modifier
                                        .clip(shape)
                                        .fillMaxWidth()
                                        .aspectRatio(6f / 9f)
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .clickable {
                                            component.onGameClicked(app)
                                        }
                                ) {
                                    Text(
                                        text = app.application.name,
                                        style = MaterialTheme.typography.labelMedium,
                                        modifier = Modifier
                                            .align(Alignment.Center)
                                            .padding(horizontal = 16.dp),
                                        textAlign = TextAlign.Center
                                    )

                                    AsyncImage(
                                        model = app.application.assets.localizedAssets[ELanguage.English]?.libraryCapsule?.path,
                                        contentDescription = null,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .aspectRatio(6f / 9f)
                                            .clip(shape),
                                        contentScale = ContentScale.FillBounds
                                    )
                                }
                            }

                            if (canLoadMore) {
                                item(span = { GridItemSpan(maxLineSpan) }) {
                                    LaunchedEffect(Unit) {
                                        component.onPageRequested()
                                    }

                                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                        LoadingIndicator()
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(
                            stringResource(R.string.library_initialization),
                            style = MaterialTheme.typography.headlineMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            stringResource(R.string.library_initialization_text),
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.fillMaxWidth()
                        )

                        LinearWavyProgressIndicator(
                            progress = { picsProgress },
                            trackColor = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
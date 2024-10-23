package bruhcollective.itaysonlab.cobalt.library.games

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Sort
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.cobalt.ui.components.BottomSheetLayout
import bruhcollective.itaysonlab.cobalt.ui.components.RoundedPage
import bruhcollective.itaysonlab.cobalt.ui.theme.partialShapes
import bruhcollective.itaysonlab.ksteam.models.enums.ELanguage
import bruhcollective.itaysonlab.ksteam.models.library.LibraryCollection
import coil.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GamesScreen(component: GamesComponent) {
    val gridState = rememberLazyGridState()
    val scrollToTopFlag by component.scrollToTopFlag.subscribeAsState()

    LaunchedEffect(scrollToTopFlag) {
        if (scrollToTopFlag) {
            gridState.animateScrollToItem(0)
            component.resetScrollToTop()
        }
    }

    val screenResult by component.screenResult.subscribeAsState()
    val currentGames by component.games.subscribeAsState()
    val currentCollections by component.collections.subscribeAsState()
    val currentCollectionId by component.currentCollectionId.subscribeAsState()
    val currentCollectionName by component.currentCollectionName.subscribeAsState()

    var tmpShowCollections by remember { mutableStateOf(false) }

    if (tmpShowCollections) {
        ModalBottomSheet(onDismissRequest = { tmpShowCollections = false }) {
            BottomSheetLayout(
                title = {
                    stringResource(R.string.library_games_chip_collections)
                }
            ) {
                LazyColumn {
                    item {
                        ListItem(headlineContent = {
                            Text(stringResource(R.string.library_games_chip_collections_default))
                        }, modifier = Modifier.fillMaxWidth().clickable {
                            component.clearCollection()
                            tmpShowCollections = false
                        }, colors = ListItemDefaults.colors(containerColor = Color.Transparent))
                    }

                    item {
                        HorizontalDivider()
                    }

                    items(currentCollections) { collection ->
                        Column {
                            ListItem(headlineContent = {
                                Text(collection.name)
                            }, modifier = Modifier.fillMaxWidth().clickable {
                                component.setCollection(collection)
                                tmpShowCollections = false
                            }, colors = ListItemDefaults.colors(containerColor = Color.Transparent), trailingContent = {
                                if (collection is LibraryCollection.Dynamic) {
                                    Icon(Icons.Rounded.Bolt, contentDescription = "Dynamic filter")
                                } else {
                                    Icon(Icons.Rounded.GridView, contentDescription = "Manual filter")
                                }
                            })

                            HorizontalDivider()
                        }
                    }
                }
            }
        }
    }

    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FilterChip(
                selected = false,
                onClick = {
                    tmpShowCollections = true
                },
                label = {
                    if (currentCollectionName.isNotEmpty()) {
                        Text(currentCollectionName)
                    } else {
                        Text(stringResource(R.string.library_games_chip_collections_default))
                    }
                },
                trailingIcon = {
                    Icon(Icons.Rounded.ArrowDropDown, contentDescription = null)
                }
            )

            Spacer(Modifier.weight(1f))

            IconButton(onClick = {

            }) {
                Icon(
                    Icons.Rounded.Tune,
                    contentDescription = stringResource(R.string.library_games_chip_filter)
                )
            }

            IconButton(onClick = {

            }) {
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
            when (screenResult) {
                CobaltScreenResult.Loading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                CobaltScreenResult.Loaded -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        state = gridState
                    ) {
                        itemsIndexed(currentGames) { index, app ->
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
                            ) {
                                Text(
                                    text = app.application.name,
                                    style = MaterialTheme.typography.labelMedium,
                                    modifier = Modifier.align(Alignment.Center).padding(horizontal = 16.dp),
                                    textAlign = TextAlign.Center
                                )

                                AsyncImage(
                                    model = app.application.assets.libraryCapsule[ELanguage.English],
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .aspectRatio(6f / 9f)
                                        .clip(shape),
                                    contentScale = ContentScale.FillBounds
                                )
                            }
                        }
                    }
                }

                CobaltScreenResult.NetworkError -> TODO()
                CobaltScreenResult.UnknownError -> TODO()
            }
        }
    }
}
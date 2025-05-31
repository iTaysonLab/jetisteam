package bruhcollective.itaysonlab.cobalt.screens.library.games.alert

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.library.games.alert.SelectCollectionComponent
import bruhcollective.itaysonlab.cobalt.ui.components.BottomSheetHeader
import bruhcollective.itaysonlab.ksteam.models.library.LibraryCollection
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ModalSelectCollectionSheet(
    onDismiss: () -> Unit,
    component: SelectCollectionComponent
) {
    val collections by component.collections.subscribeAsState()

    ModalBottomSheet(onDismissRequest = onDismiss) {
        BottomSheetHeader(
            text = stringResource(R.string.library_games_chip_collections),
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(2),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalItemSpacing = 8.dp,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            item(span = StaggeredGridItemSpan.FullLine) {
                Card {
                    Column {
                        ListItem(
                            headlineContent = {
                                Text(stringResource(R.string.library_games_chip_collections_default))
                            },
                            leadingContent = {
                                Icon(Icons.Rounded.GridView, contentDescription = stringResource(R.string.library_games_chip_collections_default))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = component::onAllClicked),
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent, leadingIconColor = MaterialTheme.colorScheme.primary)
                        )

                        HorizontalDivider()

                        ListItem(
                            headlineContent = {
                                Text(stringResource(R.string.library_games_collection_favorite))
                            },
                            leadingContent = {
                                Icon(Icons.Rounded.Favorite, contentDescription = stringResource(R.string.library_games_collection_favorite))
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable(onClick = component::onFavoritesClicked),
                            colors = ListItemDefaults.colors(containerColor = Color.Transparent, leadingIconColor = MaterialTheme.colorScheme.primary)
                        )
                    }
                }
            }

            items(collections) { collection ->
                Card(onClick = {
                    component.onCollectionClicked(collection)
                }, modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(8.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Row {
                            if (collection is LibraryCollection.Dynamic) {
                                Icon(
                                    Icons.Rounded.Bolt,
                                    contentDescription = "Dynamic filter",
                                    modifier = Modifier.size(18.dp)
                                )
                            } else {
                                Icon(
                                    Icons.Rounded.GridView,
                                    contentDescription = "Manual filter",
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Text(
                            text = remember(collection.name) {
                                collection.name.uppercase() // Steam always uppercases
                            },
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
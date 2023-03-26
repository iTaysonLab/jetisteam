package bruhcollective.itaysonlab.microapp.library.ui.library_pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Badge
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.ksteam.models.apps.AppSummary
import bruhcollective.itaysonlab.ksteam.models.apps.libraryEntry
import bruhcollective.itaysonlab.ksteam.models.library.LibraryCollection
import bruhcollective.itaysonlab.ksteam.models.pics.AppInfo
import bruhcollective.itaysonlab.ksteam.models.pics.header
import bruhcollective.itaysonlab.microapp.library.R
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SimpleShelf(
    collectionDelegate: () -> Flow<LibraryCollection>,
    collectionGamesDelegate: () -> Flow<ImmutableList<AppSummary>>,
    onClick: (Int) -> Unit
) {
    val collection by collectionDelegate().collectAsStateWithLifecycle(initialValue = LibraryCollection.Placeholder, context = Dispatchers.IO)
    val collectionGames by collectionGamesDelegate().collectAsStateWithLifecycle(initialValue = persistentListOf(), context = Dispatchers.IO)

    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = collection.name, style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.width(8.dp))

            Badge {
                Text(text = collectionGames.size.toString())
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = null)
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            if (collectionGames.isNotEmpty()) {
                items(collectionGames) { app ->
                    LibraryItem(
                        app.libraryEntry.url,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(225.dp)
                            .width(150.dp)
                            .clickable {
                                onClick(app.id.id)
                            },
                        placeholderColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                    )
                }
            } else {
                // TODO: I certainly know that there should be a better way, but for now let's just show a placeholder
                items(6) {
                    LibraryItem(
                        "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(225.dp)
                            .width(150.dp),
                        placeholderColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
    }
}

@Composable
internal fun PlayNextShelf(
    games: ImmutableList<AppInfo>,
    onClick: (Int) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(
                    text = stringResource(id = R.string.library_shelf_play_next),
                    style = MaterialTheme.typography.headlineSmall
                )

                Text(
                    text = stringResource(id = R.string.library_shelf_play_next_summary),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp),
            modifier = Modifier
        ) {
            items(games, key = { it.appId }, contentType = { 0 }) { app ->
                LibraryItem(
                    app.header.url,
                    modifier = Modifier
                        .width(276.dp)
                        .height(129.dp)
                        .clickable {
                            onClick(app.appId)
                        },
                    placeholderColor = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun CollectionsShelf(
    collections: ImmutableList<LibraryCollection>,
    onCollectionClicked: (String) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {

        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = stringResource(id = R.string.library_shelf_all_collections),
                style = MaterialTheme.typography.headlineSmall
            )

            Spacer(modifier = Modifier.width(8.dp))

            Badge {
                Text(text = "8")
            }

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Rounded.Edit, contentDescription = null)
            }
        }

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(collections) { collection ->
                Box(
                    Modifier.clip(RoundedCornerShape(8.dp)).background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp)).size(140.dp).clickable {
                        onCollectionClicked(collection.id)
                    }
                ) {
                    Text(text = collection.name, modifier = Modifier.align(Alignment.Center).padding(8.dp), textAlign = TextAlign.Center)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
    }
}
package bruhcollective.itaysonlab.microapp.library.ui.library_pages

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ChevronRight
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.ksteam.handlers.library
import bruhcollective.itaysonlab.ksteam.handlers.pics
import bruhcollective.itaysonlab.ksteam.models.AppId
import bruhcollective.itaysonlab.ksteam.models.apps.AppSummary
import bruhcollective.itaysonlab.ksteam.models.apps.libraryEntry
import bruhcollective.itaysonlab.ksteam.models.library.LibraryCollection
import bruhcollective.itaysonlab.ksteam.models.pics.AppInfo
import bruhcollective.itaysonlab.ksteam.models.pics.header
import bruhcollective.itaysonlab.microapp.library.R
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@Composable
internal fun Homescreen(
    onClick: (Int) -> Unit,
    viewModel: HomescreenViewModel = hiltViewModel()
) {
    val shelves = viewModel.homeScreenShelves.collectAsStateWithLifecycle(context = Dispatchers.IO)

    LazyColumn(Modifier.fillMaxSize()) {
        items(shelves.value, key = { it.id }) { shelf ->
            when (shelf.linkedCollection) {
                "all-collections" -> {
                    ExampleCollectionsShelf()
                }

                "favorite" -> {
                    val collectionGames = viewModel.getFavoriteApps()
                        .collectAsStateWithLifecycle(initialValue = emptyList(), context = Dispatchers.IO)

                    SimpleShelf(
                        collectionName = stringResource(id = R.string.library_shelf_favorite),
                        collectionGames = collectionGames.value,
                        onClick = onClick
                    )
                }

                "recent-games" -> {
                    val collectionGames = viewModel.getRecentApps()
                        .collectAsStateWithLifecycle(initialValue = emptyList(), context = Dispatchers.IO)

                    SimpleShelf(
                        collectionName = stringResource(id = R.string.library_shelf_recent),
                        collectionGames = collectionGames.value,
                        onClick = onClick
                    )
                }

                "play-next" -> {
                    PlayNextShelf(viewModel.nextPlayGames, onClick = onClick)
                }

                "type-games" -> {
                    Card(Modifier.fillMaxWidth()) {
                        Text(text = "All Games", Modifier.padding(16.dp))
                    }
                }

                else -> {
                    val collection =
                        viewModel.getCollection(shelf.linkedCollection).collectAsStateWithLifecycle(
                            initialValue = LibraryCollection(
                                "",
                                "",
                                emptyList(),
                                emptyList(),
                                null,
                                0,
                                0L
                            ), context = Dispatchers.IO
                        )

                    val collectionGames = viewModel.getCollectionApps(shelf.linkedCollection)
                        .collectAsStateWithLifecycle(initialValue = emptyList(), context = Dispatchers.IO)

                    SimpleShelf(
                        collectionName = collection.value.name,
                        collectionGames = collectionGames.value,
                        onClick = onClick
                    )
                }
            }
        }
    }
}

@HiltViewModel
internal class HomescreenViewModel @Inject constructor(
    private val steamClient: HostSteamClient
) : ViewModel() {
    var nextPlayGames by mutableStateOf<List<AppInfo>>(emptyList())
        private set

    init {
        viewModelScope.launch {
            nextPlayGames = getPlayNextQueue()
        }
    }

    private suspend fun getPlayNextQueue() = steamClient.client.pics.getAppIdsAsInfos(steamClient.client.library.getPlayNextQueue().map(::AppId))
    fun getCollection(id: String) = steamClient.client.library.getCollection(id)
    fun getCollectionApps(id: String) = steamClient.client.library.getAppsInCollection(id)
    fun getFavoriteApps() = steamClient.client.library.getFavoriteApps()
    fun getRecentApps() = steamClient.client.library.getRecentApps()
    val homeScreenShelves get() = steamClient.client.library.shelves
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SimpleShelf(
    collectionName: String,
    collectionGames: List<AppSummary>,
    onClick: (Int) -> Unit
) {
    Column(Modifier.fillMaxWidth()) {
        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = collectionName, style = MaterialTheme.typography.headlineSmall)

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
                            .width(150.dp).clickable {
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
    games: List<AppInfo>,
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
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {
            items(games) { app ->
                LibraryItem(
                    app.header.url,
                    modifier = Modifier
                        .width(276.dp)
                        .height(129.dp).clickable {
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
internal fun ExampleCollectionsShelf() {
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
            items(6) {
                Box(
                    Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
                        .size(140.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider(color = MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
    }
}
package bruhcollective.itaysonlab.microapp.library.ui.library_pages

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.jetisteam.uikit.components.IndicatorBehindScrollableTabRow
import bruhcollective.itaysonlab.jetisteam.uikit.components.tabIndicatorOffset
import bruhcollective.itaysonlab.ksteam.models.library.LibraryCollection
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.library.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun LibraryMainContainer(
    onApplicationClick: (Int) -> Unit,
    viewModel: LibraryMainContainerViewModel = hiltViewModel()
) {
    val sortedCollections by viewModel.sortedUserCollections.collectAsStateWithLifecycle(context = Dispatchers.IO)
    val favorites by viewModel.favorites.collectAsStateWithLifecycle(context = Dispatchers.IO)
    val recentApps by viewModel.recents.collectAsStateWithLifecycle(context = Dispatchers.IO)

    val scope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState()

    val onCollectionClick: (String) -> Unit = { id ->
        scope.launch {
            val targetPage = withContext(Dispatchers.Default) {
                sortedCollections.indexOfFirst { it.id == id } + 1
            }

            pagerState.animateScrollToPage(targetPage)
        }
    }

    Scaffold(topBar = {
        IndicatorBehindScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            indicator = { tabPositions ->
                Box(
                    Modifier
                        .padding(vertical = 12.dp)
                        .tabIndicatorOffset(tabPositions[pagerState.currentPage])
                        .fillMaxHeight()
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface)
                )
            },
            edgePadding = 16.dp,
            modifier = Modifier.statusBarsPadding(),
            tabAlignment = Alignment.Center
        ) {
            Tab(
                selected = pagerState.currentPage == 0,
                onClick = {
                    scope.launch {
                        pagerState.animateScrollToPage(0)
                    }
                },
                selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
            ) {
                Icon(
                    imageVector = Icons.Rounded.Home,
                    contentDescription = null,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }

            sortedCollections.forEachIndexed { index, collection ->
                Tab(
                    selected = pagerState.currentPage == index + 1,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index + 1)
                        }
                    },
                    selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    Text(
                        text = collection.name.uppercase(Locale.getDefault()),
                        modifier = Modifier.padding(vertical = 20.dp, horizontal = 12.dp),
                        fontWeight = FontWeight.Medium,
                        fontSize = 15.sp
                    )
                }
            }
        }
    }, contentWindowInsets = EmptyWindowInsets, snackbarHost = {
        SnackbarHost(hostState = snackState) {
            Snackbar(snackbarData = it)
        }
    }, modifier = Modifier.fillMaxSize()) { innerPadding ->
        HorizontalPager(
            pageCount = sortedCollections.size + 1, state = pagerState, modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), beyondBoundsPageCount = 1
        ) { page ->
            if (page == 0) {
                LazyColumn(Modifier.fillMaxSize()) {
                    items(viewModel.homeScreenShelves, key = { it.id }) { shelf ->
                        when (shelf.linkedCollection) {
                            "all-collections" -> {
                                CollectionsShelf(sortedCollections, onCollectionClick)
                            }

                            "favorite" -> {
                                SimpleShelf(
                                    collectionName = stringResource(id = R.string.library_shelf_favorite),
                                    collectionGames = favorites,
                                    onClick = onApplicationClick
                                )
                            }

                            "recent-games" -> {
                                SimpleShelf(
                                    collectionName = stringResource(id = R.string.library_shelf_recent),
                                    collectionGames = recentApps,
                                    onClick = onApplicationClick
                                )
                            }

                            "play-next" -> {
                                PlayNextShelf(viewModel.nextPlayGames, onClick = onApplicationClick)
                            }

                            "type-games" -> {
                                Card(Modifier.fillMaxWidth()) {
                                    Text(text = "All Games", Modifier.padding(16.dp))
                                }
                            }

                            else -> {
                                val collection by viewModel.getCollection(shelf.linkedCollection).collectAsStateWithLifecycle(initialValue = LibraryCollection.Placeholder, context = Dispatchers.IO)
                                val collectionGames by viewModel.getCollectionApps(shelf.linkedCollection).collectAsStateWithLifecycle(initialValue = emptyList(), context = Dispatchers.IO)

                                SimpleShelf(
                                    collectionName = collection.name,
                                    collectionGames = collectionGames,
                                    onClick = onApplicationClick
                                )
                            }
                        }
                    }
                }
            } else {
                val summaries by viewModel.getCollectionApps(sortedCollections[page - 1].id)
                    .collectAsStateWithLifecycle(
                        initialValue = emptyList(),
                        context = Dispatchers.IO
                    )

                CollectionPage(
                    summaries = summaries,
                    onClick = onApplicationClick
                )
            }
        }
    }
}
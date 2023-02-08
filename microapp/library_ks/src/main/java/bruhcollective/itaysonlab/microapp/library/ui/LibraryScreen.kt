package bruhcollective.itaysonlab.microapp.library.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.Badge
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.IndicatorBehindScrollableTabRow
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.components.tabIndicatorOffset
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import coil.compose.AsyncImage
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(
    ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalFoundationApi::class
)
@Composable
internal fun LibraryScreen(
    onGameClick: (Long, String) -> Unit,
    onBackClick: () -> Unit,
    // onRemoteClick: (Long, List<CClientComm_GetAllClientLogonInfo_Response_Session>) -> Unit,
    viewModel: LibraryScreenViewModel = hiltViewModel()
) {
    var sortByMenuOpened by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()
    val snackState = remember { SnackbarHostState() }
    val pagerState = rememberPagerState()
    val data = remember { mutableListOf("", "", "", "", "", "", "", "", "", "", "", "") }

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

            repeat(5) {
                Tab(
                    selected = pagerState.currentPage == it + 1,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(it + 1)
                        }
                    },
                    selectedContentColor = MaterialTheme.colorScheme.inverseOnSurface,
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                ) {
                    Text(
                        text = "Collection $it".uppercase(Locale.getDefault()),
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
            pageCount = 6, state = pagerState, modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) { page ->
            if (page == 0) {
                LazyColumn(Modifier.fillMaxSize()) {
                    item {
                        ExampleCollectionsShelf()
                    }

                    item {
                        ExamplePlayNextShelf()
                    }

                    item {
                        ExampleShelf()
                    }
                }
            } else {
                RoundedPage(modifier = Modifier.fillMaxSize()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(data) { index, game ->
                            LibraryItem(game, modifier = Modifier
                                .fillMaxWidth()
                                .height(180.dp)
                                .let {
                                    when (index) {
                                        0 -> it.clip(
                                            MaterialTheme.shapes.large.copy(
                                                topEnd = CornerSize(0.dp),
                                                bottomStart = CornerSize(0.dp),
                                                bottomEnd = CornerSize(0.dp)
                                            )
                                        )

                                        2 -> it.clip(
                                            MaterialTheme.shapes.large.copy(
                                                topStart = CornerSize(0.dp),
                                                bottomStart = CornerSize(0.dp),
                                                bottomEnd = CornerSize(0.dp)
                                            )
                                        )

                                        else -> it
                                    }
                                }
                                .clickable {

                                })
                        }
                    }
                }
            }
        }
    }
}

@Composable
internal fun LibraryItem(
    image: String,
    modifier: Modifier,
    placeholderColor: Color = MaterialTheme.colorScheme.surface
) {
    AsyncImage(
        model = image,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.FillBounds,
        placeholder = ColorPainter(placeholderColor),
        error = ColorPainter(placeholderColor),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ExampleShelf() {
    Column(Modifier.fillMaxWidth()) {

        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Shelf", style = MaterialTheme.typography.headlineSmall)

            Spacer(modifier = Modifier.width(8.dp))

            Badge {
                Text(text = "4")
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
                LibraryItem(
                    "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .width(140.dp),
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
internal fun ExamplePlayNextShelf() {
    Column(Modifier.fillMaxWidth()) {

        Row(Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Column {
                Text(text = "Play Next", style = MaterialTheme.typography.headlineSmall)
                Text(
                    text = "Players like you love these unplayed games in your library",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
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
                LibraryItem(
                    "",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .width(240.dp),
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
            Text(text = "Collections View", style = MaterialTheme.typography.headlineSmall)

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
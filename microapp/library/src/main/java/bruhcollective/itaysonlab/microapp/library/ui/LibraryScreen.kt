package bruhcollective.itaysonlab.microapp.library.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LibraryScreen(
    onGameClick: (Long, Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: LibraryScreenViewModel = hiltViewModel()
) {
    PageLayout(state = viewModel.state, onReload = viewModel::reload) { data ->
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Surface(tonalElevation = 16.dp, modifier = Modifier
                        .fillMaxWidth()
                        .height(64.dp - 24.dp), shape = MaterialTheme.shapes.medium) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 12.dp)) {
                            Icon(imageVector = Icons.Rounded.Search, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Search", color = MaterialTheme.colorScheme.onSurfaceVariant, fontSize = 15.sp)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent),
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
                    }
                }
            )
        }, contentWindowInsets = EmptyWindowInsets) { innerPadding ->
            Column(Modifier.padding(innerPadding)) {
                if (viewModel.machines.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(tonalElevation = 1.dp, modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp), shape = MaterialTheme.shapes.medium) {
                        ListItem(
                            headlineText = {
                                Text("${viewModel.machines.size} remote PC is available")
                            }, leadingContent = {
                                Icon(imageVector = Icons.Rounded.SettingsRemote, contentDescription = null)
                            }, supportingText = {
                                Text(viewModel.machines.first().machine_name.orEmpty())
                            }, trailingContent = {
                                Icon(imageVector = Icons.Rounded.ChevronRight, contentDescription = null)
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                RoundedPage(modifier = Modifier.fillMaxSize()) {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(3),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(data) { index, game ->
                            LibraryItem(game.capsule, modifier = Modifier
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
                                .clickable { onGameClick(viewModel.longSteamId, game.appid) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LibraryItem(
    image: String,
    modifier: Modifier,
) {
    AsyncImage(
        model = image,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.FillBounds,
        placeholder = ColorPainter(MaterialTheme.colorScheme.surface)
    )
}
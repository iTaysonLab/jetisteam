package bruhcollective.itaysonlab.microapp.library.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenPlaceholder
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.microapp.core.ext.EmptyWindowInsets
import bruhcollective.itaysonlab.microapp.core.ext.asBase64
import bruhcollective.itaysonlab.microapp.library.R
import coil.compose.AsyncImage
import steam.clientcomm.CClientComm_GetAllClientLogonInfo_Response_Session

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
internal fun LibraryScreen(
    onGameClick: (Long, String) -> Unit,
    onBackClick: () -> Unit,
    onRemoteClick: (Long, List<CClientComm_GetAllClientLogonInfo_Response_Session>) -> Unit,
    viewModel: LibraryScreenViewModel = hiltViewModel()
) {
    var sortByMenuOpened by remember { mutableStateOf(false) }

    PageLayout(state = viewModel.state, onReload = viewModel::reload) { data ->
        Scaffold(topBar = {
            TopAppBar(
                title = {
                    Surface(
                        tonalElevation = 16.dp, modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp - 24.dp), shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 12.dp)
                        ) {
                            Icon(imageVector = Icons.Rounded.Search, contentDescription = null)

                            Spacer(modifier = Modifier.width(8.dp))

                            BasicTextField(
                                value = viewModel.searchQuery,
                                onValueChange = {
                                    viewModel.searchQuery = it
                                    viewModel.notifySortReload()
                                },
                                textStyle = TextStyle.Default.copy(
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 15.sp
                                ),
                                maxLines = 1,
                                decorationBox = { innerTextField ->
                                    if (viewModel.searchQuery.text.isEmpty()) {
                                        Text(
                                            text = stringResource(id = R.string.library_search_hint),
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            fontSize = 15.sp
                                        )
                                    }

                                    innerTextField()
                                },
                                modifier = Modifier.fillMaxWidth(),
                                cursorBrush = SolidColor(MaterialTheme.colorScheme.primary)
                            )
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
                    IconButton(onClick = { sortByMenuOpened = true }) {
                        Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = null)
                    }

                    DropdownMenu(
                        expanded = sortByMenuOpened,
                        onDismissRequest = { sortByMenuOpened = false }) {
                        Text(
                            text = stringResource(id = R.string.library_sort),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )

                        LibraryScreenViewModel.SortMode.values().forEach { mode ->
                            DropdownMenuItem(text = {
                                Text(text = stringResource(id = mode.nameRes))
                            }, leadingIcon = {
                                Icon(
                                    imageVector = mode.iconFunc(),
                                    contentDescription = null
                                )
                            }, onClick = {
                                sortByMenuOpened = false
                                viewModel.sortMode = mode
                                viewModel.notifySortReload()
                            }, trailingIcon = {
                                Icon(
                                    imageVector = Icons.Rounded.Check,
                                    contentDescription = null,
                                    modifier = Modifier.alpha(
                                        if (viewModel.sortMode == mode) 1f else 0f
                                    )
                                )
                            })
                        }
                    }
                }
            )
        }, contentWindowInsets = EmptyWindowInsets) { innerPadding ->
            Column(Modifier.padding(innerPadding)) {
                if (viewModel.library.machines.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))

                    Surface(
                        tonalElevation = 1.dp, modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp), shape = MaterialTheme.shapes.medium
                    ) {
                        ListItem(
                            headlineContent = {
                                Text(
                                    text = pluralStringResource(
                                        id = R.plurals.library_remote_pcs,
                                        count = viewModel.library.machines.size,
                                        viewModel.library.machines.size
                                    )
                                )
                            }, leadingContent = {
                                Icon(
                                    imageVector = Icons.Rounded.SettingsRemote,
                                    contentDescription = null
                                )
                            }, supportingContent = {
                                Text(remember(viewModel.library.machines) {
                                    viewModel.library.machines.joinToString { it.machine_name.orEmpty() }
                                })
                            }, trailingContent = {
                                Icon(
                                    imageVector = Icons.Rounded.ChevronRight,
                                    contentDescription = null
                                )
                            }, modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onRemoteClick(
                                        viewModel.longSteamId,
                                        viewModel.library.machines
                                    )
                                }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }

                RoundedPage(modifier = Modifier.fillMaxSize()) {
                    if (data.isNotEmpty()) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(3),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize()
                        ) {
                            itemsIndexed(data) { index, game ->
                                LibraryItem(game.capsuleUrl, modifier = Modifier
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
                                        onGameClick(
                                            viewModel.longSteamId,
                                            game.proto.asBase64()
                                        )
                                    })
                            }
                        }
                    } else {
                        val isSearch = viewModel.searchQuery.text.isNotEmpty()

                        FullscreenPlaceholder(
                            icon = Icons.Rounded.CleaningServices,
                            title = stringResource(
                                id = if (isSearch) {
                                    R.string.library_remote_library_search_empty
                                } else {
                                    R.string.library_remote_library_empty
                                }
                            ),
                            text = stringResource(
                                id = if (isSearch) {
                                    R.string.library_remote_library_search_empty_text
                                } else {
                                    R.string.library_remote_library_empty_text
                                }
                            )
                        )
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
) {
    AsyncImage(
        model = image,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.FillBounds,
        placeholder = ColorPainter(MaterialTheme.colorScheme.surface),
        error = ColorPainter(MaterialTheme.colorScheme.surface),
    )
}
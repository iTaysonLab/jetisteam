package bruhcollective.itaysonlab.microapp.library.ui.remote.pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CleaningServices
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.uikit.page.FullscreenPlaceholder
import bruhcollective.itaysonlab.jetisteam.util.CdnUrlUtil
import bruhcollective.itaysonlab.microapp.library.R
import bruhcollective.itaysonlab.microapp.library.ui.LibraryItem
import steam.clientcomm.CClientComm_GetClientAppList_Response_AppData

@Composable
internal fun LibraryPage(
    apps: List<CClientComm_GetClientAppList_Response_AppData>,
    onClick: (Int, String, Int) -> Unit
) {
    if (apps.isEmpty()) {
        FullscreenPlaceholder(
            icon = Icons.Rounded.CleaningServices,
            title = stringResource(
                R.string.library_remote_library_empty
            ),
            text = stringResource(
                R.string.library_remote_library_empty_text
            )
        )
    } else {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = floatingWindowInsetsAsPaddings(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(apps) { index, game ->
                var showMenu by remember {
                    mutableStateOf(false)
                }

                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(180.dp)) {
                    LibraryItem(CdnUrlUtil.buildAppUrl(game.appid ?: 0, "library_600x900.jpg"), modifier = Modifier
                        .fillMaxSize()
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
                            showMenu = true
                        })

                    DropdownMenu(expanded = showMenu, onDismissRequest = { showMenu = false }) {
                        DropdownMenuItem(text = {
                            Text(text = stringResource(id = R.string.library_sheet_store))
                        }, onClick = {
                            showMenu = false
                            onClick(game.appid ?: 0, game.app.orEmpty(), 0)
                        }, leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.ShoppingCart,
                                contentDescription = null
                            )
                        })

                        DropdownMenuItem(text = {
                            Text(text = stringResource(id = R.string.library_remote_uninstall))
                        }, onClick = {
                            showMenu = false
                            onClick(game.appid ?: 0, game.app.orEmpty(), 1)
                        }, leadingIcon = {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = null
                            )
                        })
                    }
                }
            }
        }
    }
}
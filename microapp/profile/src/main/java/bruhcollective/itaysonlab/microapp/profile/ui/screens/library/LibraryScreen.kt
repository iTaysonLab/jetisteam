package bruhcollective.itaysonlab.microapp.profile.ui.screens.library

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.microapp.profile.R
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun LibraryScreen(
    onGameClick: (Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: LibraryScreenViewModel = hiltViewModel()
) {
    PageLayout(state = viewModel.state, onReload = viewModel::reload) { data ->
        Column {
            SmallTopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.library))
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.Rounded.ArrowBack, contentDescription = null)
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Transparent)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .clip(RoundedCornerShape(topStart = 12.dp, topEnd = 12.dp))
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(4.dp))
            ) {
                items(data) { game ->
                    LibraryItem(game.capsule, modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clickable { onGameClick(game.appid) })
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
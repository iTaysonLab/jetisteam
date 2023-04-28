package bruhcollective.itaysonlab.microapp.library.ui.library_pages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import bruhcollective.itaysonlab.jetisteam.uikit.components.RoundedPage
import bruhcollective.itaysonlab.jetisteam.uikit.floatingWindowInsetsAsPaddings
import bruhcollective.itaysonlab.jetisteam.uikit.partialShapes
import bruhcollective.itaysonlab.jetisteam.util.CdnUrlUtil
import bruhcollective.itaysonlab.ksteam.models.library.OwnedGame
import coil.compose.AsyncImage
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow

@Composable
internal fun CollectionPage(
    onClick: (Int) -> Unit,
    summariesDelegate: () -> Flow<ImmutableList<OwnedGame>>,
) {
    val summaries by summariesDelegate().collectAsStateWithLifecycle(
        initialValue = persistentListOf(),
        context = Dispatchers.IO
    )

    RoundedPage(modifier = Modifier.fillMaxSize()) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(3),
            contentPadding = floatingWindowInsetsAsPaddings(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(summaries, key = { _, app ->
                app.id.id
            }, contentType = { _, _ ->
                0
            }) { index, app ->
                LibraryItem(remember(app.id) {
                    CdnUrlUtil.buildAppUrl(app.id.id, if (app.capsuleFilename.isNotEmpty()) {
                        app.capsuleFilename
                    } else {
                        "library_600x900.jpg"
                    })
                }, modifier = Modifier
                    .aspectRatio(2f / 3f)
                    .clip(
                        when (index) {
                            0 -> MaterialTheme.partialShapes.largeTopLeftShape
                            2 -> MaterialTheme.partialShapes.largeTopRightShape
                            else -> RectangleShape
                        }
                    )
                    .clickable { onClick(app.id.id) }
                )
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
    val placeholderPainter = remember(placeholderColor) {
        ColorPainter(placeholderColor)
    }

    AsyncImage(
        model = image,
        contentDescription = null,
        modifier = modifier,
        contentScale = ContentScale.FillBounds,
        placeholder = placeholderPainter,
        error = placeholderPainter,
    )
}
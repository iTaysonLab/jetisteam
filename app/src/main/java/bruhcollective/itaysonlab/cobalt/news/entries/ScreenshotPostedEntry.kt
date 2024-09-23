package bruhcollective.itaysonlab.cobalt.news.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.news.entries.parts.PostPersonaHeader
import bruhcollective.itaysonlab.cobalt.ui.font.rubikFontFamily
import bruhcollective.itaysonlab.ksteam.models.apps.capsuleSmall
import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry
import coil.compose.AsyncImage

@Composable
fun ScreenshotPostedEntry(
    entry: ActivityFeedEntry.ScreenshotPosted
) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PostPersonaHeader(
            persona = entry.persona,
            postedDate = entry.date
        )

        Text(
            text = "posted a screenshot of ${entry.app.name}",
            style = MaterialTheme.typography.labelMedium,
            fontFamily = rubikFontFamily
        )

        AsyncImage(
            model = entry.screenshot.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp),
            contentScale = ContentScale.Crop,
            placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
            error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
        )
    }
}
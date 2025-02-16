package bruhcollective.itaysonlab.cobalt.screens.news.entries

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.screens.news.entries.parts.PostPersonaHeader
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
        )

        AsyncImage(
            model = entry.screenshot.imageUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(MaterialTheme.shapes.large),
            contentScale = ContentScale.Crop,
            placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
            error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
        )
    }
}

@Composable
fun ScreenshotsPostedEntry(
    entry: ActivityFeedEntry.ScreenshotsPosted
) {
    val pagerState = rememberPagerState { entry.screenshots.size }

    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PostPersonaHeader(
            persona = entry.persona,
            postedDate = entry.date
        )

        Text(
            text = "posted screenshots of ${entry.app.name}",
            style = MaterialTheme.typography.labelMedium,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(MaterialTheme.shapes.large)
        ) {
            HorizontalPager(state = pagerState) { index ->
                AsyncImage(
                    model = entry.screenshots[index].imageUrl,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                    error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black)
                        )
                    )
                    .padding(8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { iteration ->
                    val alpha = if (pagerState.currentPage == iteration) {
                        1f
                    } else {
                        0.5f
                    }

                    Box(
                        modifier = Modifier
                            .padding(2.dp)
                            .alpha(alpha)
                            .clip(CircleShape)
                            .background(Color.White)
                            .size(8.dp)
                    )
                }
            }
        }
    }
}
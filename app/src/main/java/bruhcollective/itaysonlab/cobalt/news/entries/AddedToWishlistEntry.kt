package bruhcollective.itaysonlab.cobalt.news.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.news.entries.parts.PostPersonaHeader
import bruhcollective.itaysonlab.ksteam.models.apps.capsuleSmall
import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry
import coil.compose.AsyncImage

@Composable
fun AddedToWishlistEntry(
    entry: ActivityFeedEntry.AddedToWishlist
) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PostPersonaHeader(
            persona = entry.persona,
            postedDate = entry.date
        )

        Text(
            text = remember { if (entry.apps.size > 1) "added ${entry.apps.size} games to wishlist" else "added ${entry.apps.firstOrNull()?.name} to wishlist" },
            style = MaterialTheme.typography.labelMedium,
        )

        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(entry.apps) { appSummary ->
                AsyncImage(model = appSummary.capsuleSmall, contentDescription = null, modifier = Modifier.width(120.dp).height(50.dp))
            }
        }
    }
}
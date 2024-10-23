package bruhcollective.itaysonlab.cobalt.news.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.news.entries.parts.PostPersonaHeader
import bruhcollective.itaysonlab.ksteam.models.apps.capsuleSmall
import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry
import coil.compose.AsyncImage

@Composable
fun PostedStatusEntry(
    entry: ActivityFeedEntry.PostedStatus
) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PostPersonaHeader(
            persona = entry.persona,
            postedDate = entry.date
        )

        Text(
            text = "wrote about ${entry.app.name}",
            style = MaterialTheme.typography.labelMedium,
        )

        AsyncImage(model = entry.app.capsuleSmall, contentDescription = null, modifier = Modifier.width(120.dp).height(50.dp))

        Text(
            text = entry.text,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}
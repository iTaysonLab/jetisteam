package bruhcollective.itaysonlab.cobalt.news.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.news.entries.parts.PostPersonaHeader
import bruhcollective.itaysonlab.cobalt.ui.font.rubikFontFamily
import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry

@Composable
fun PlayedForFirstTimeEntry(
    entry: ActivityFeedEntry.PlayedForFirstTime
) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PostPersonaHeader(
            persona = entry.persona,
            postedDate = entry.date
        )

        Text(
            text = remember { "played ${entry.app.name} for the first time" },
            style = MaterialTheme.typography.labelMedium,
            fontFamily = rubikFontFamily
        )
    }
}
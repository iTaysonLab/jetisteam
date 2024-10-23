package bruhcollective.itaysonlab.cobalt.news.entries

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.news.entries.parts.PostPersonaHeader
import bruhcollective.itaysonlab.cobalt.ui.components.CobaltDivider
import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry
import coil.compose.AsyncImage

@Composable
fun NewAchievementsEntry(
    entry: ActivityFeedEntry.NewAchievements
) {
    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        PostPersonaHeader(
            persona = entry.persona,
            postedDate = entry.date
        )

        Text(
            text = "achieved in ${entry.app.name}",
            style = MaterialTheme.typography.labelMedium,
        )

        entry.achievements.forEachIndexed { index, achievement ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                AsyncImage(
                    model = remember(achievement.internalName) {
                        achievement.icon
                    },
                    contentDescription = null,
                    modifier = Modifier.size(36.dp)
                )

                Column {
                    Text(text = achievement.displayName)

                    Text(
                        text = achievement.displayDescription,
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.alpha(0.5f)
                    )
                }
            }

            if (index != entry.achievements.lastIndex) {
                CobaltDivider(padding = 0.dp)
            }
        }
    }
}
package bruhcollective.itaysonlab.jetisteam.news.entries

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry

@Composable
fun NewAchievementsEntry(
    entry: ActivityFeedEntry.NewAchievements
) {
    Column {
        entry.achievements.forEach {
            Text(it.displayName)
        }
    }
}
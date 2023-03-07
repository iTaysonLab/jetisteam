package bruhcollective.itaysonlab.microapp.library.ui.game_page.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.page.PageLayout
import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry
import coil.compose.AsyncImage

@Composable
internal fun GamepageActivityScreen(
    viewModel: GamepageActivityViewModel = hiltViewModel()
) {
    PageLayout(viewModel.state) { list ->
        LazyColumn(
            Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(list) { entry ->
                when (entry) {
                    is GamepageActivityViewModel.ActivityItem.DateSeparator -> {
                        Text(entry.parsedDate, style = MaterialTheme.typography.headlineMedium)
                    }

                    is GamepageActivityViewModel.ActivityItem.EntryContainer -> {
                        when (val activityCard = entry.item) {
                            is ActivityFeedEntry.NewAchievements -> {
                                ActivityCardNewAchievements(entry = activityCard)
                            }

                            is ActivityFeedEntry.PlayedForFirstTime -> {

                            }

                            is ActivityFeedEntry.PostedAnnouncement -> {

                            }

                            is ActivityFeedEntry.ReceivedNewGame -> {

                            }

                            is ActivityFeedEntry.UnknownEvent -> {

                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ActivityCardBaseHeader(
    avatarUrl: String,
    name: String,
    subtitle: String
) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            modifier = Modifier.size(56.dp),
            model = avatarUrl,
            contentDescription = null,
        )

        Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(text = name, style = MaterialTheme.typography.bodyMedium)
            Text(text = subtitle, style = MaterialTheme.typography.bodySmall)
        }
    }
}

@Composable
private fun ActivityCardBase(
    header: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(8.dp)
        )
    ) {
        header()
        content()
    }
}

//

@Composable
private fun ActivityCardNewAchievements(
    entry: ActivityFeedEntry.NewAchievements
) {
    ActivityCardBase(
        header = {
            ActivityCardBaseHeader(
                avatarUrl = entry.persona.avatar.medium,
                name = entry.persona.name,
                subtitle = if (entry.achievements.size > 1) {
                    "gained new achievements"
                } else {
                    "gained new achievement"
                }
            )
        }
    ) {
        entry.achievements.forEach { achievement ->
            ListItem(
                headlineText = {
                    Text(achievement.displayName)
                },
                supportingText = {
                    Text(achievement.displayDescription)
                },
                colors = ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ActivityCardPlayedForTheFirstTime(

) {

}
package bruhcollective.itaysonlab.jetisteam.news.discover

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.news.discover.DiscoverComponent
import bruhcollective.itaysonlab.jetisteam.news.entries.NewAchievementsEntry
import bruhcollective.itaysonlab.jetisteam.news.entries.PlayedForFirstTimeEntry
import bruhcollective.itaysonlab.jetisteam.news.entries.ReceivedNewGameEntry
import bruhcollective.itaysonlab.jetisteam.ui.components.CobaltDivider
import bruhcollective.itaysonlab.jetisteam.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.jetisteam.ui.components.rememberFloatingNavigationBarScrollConnection
import bruhcollective.itaysonlab.jetisteam.ui.font.robotoMonoFontFamily
import bruhcollective.itaysonlab.jetisteam.ui.font.rubikFontFamily
import bruhcollective.itaysonlab.ksteam.models.apps.icon
import bruhcollective.itaysonlab.ksteam.models.news.NewsEvent
import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry
import coil.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.collections.immutable.ImmutableList

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverScreen(
    component: DiscoverComponent
) {
    val loadState by component.state.subscribeAsState()
    val items by component.items.subscribeAsState()

    Scaffold(
        topBar = {
            Column {
                TopAppBar(
                    title = {
                        Text(text = "News".uppercase(), fontFamily = robotoMonoFontFamily)
                    }, colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )

                CobaltDivider(padding = 0.dp)
            }
        }, contentWindowInsets = EmptyWindowInsets
    ) { innerPadding ->
        when (loadState) {
            DiscoverComponent.DiscoverState.Idle -> {
                LaunchedEffect(Unit) {
                    component.dispatchInitialLoad()
                }
            }

            DiscoverComponent.DiscoverState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding), contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            DiscoverComponent.DiscoverState.Loaded -> {
                LazyColumn(
                    modifier = Modifier
                        .padding(innerPadding)
                        .nestedScroll(rememberFloatingNavigationBarScrollConnection())
                ) {
                    /*item {
                        FusionOnboardingCard()
                    }*/

                    items(items, key = DiscoverComponent.DiscoverItem::id, contentType = DiscoverComponent.DiscoverItem::contentType) { item ->
                        when (item) {
                            is DiscoverComponent.DiscoverItem.ActivityFeed -> ActivityFeedPortal(
                                feedEntry = item.entry
                            )

                            is DiscoverComponent.DiscoverItem.NewsPost -> NewsEventPortal(item.entry)

                            is DiscoverComponent.DiscoverItem.UpcomingEvents -> UpcomingEvents(
                                events = item.events
                            )
                        }
                    }
                }
            }

            DiscoverComponent.DiscoverState.Error -> TODO()
        }
    }
}

@Composable
fun NewsEventPortal(entry: NewsEvent) {
    Column(modifier = Modifier.padding(16.dp)) {
        val ctx = LocalContext.current

        val formattedDate = remember(entry.publishedAt) {
            DateUtils.getRelativeDateTimeString(
                ctx,
                entry.publishedAt * 1000L,
                DateUtils.MINUTE_IN_MILLIS,
                DateUtils.WEEK_IN_MILLIS,
                0
            ).toString().uppercase()
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            AsyncImage(
                model = entry.relatedApp?.icon,
                contentDescription = null,
                modifier = Modifier.size(38.dp),
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.FillBounds
            )

            Column {
                Text(text = entry.relatedApp?.name.orEmpty(), maxLines = 1, overflow = TextOverflow.Ellipsis)

                Text(
                    text = formattedDate,
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.alpha(0.5f), maxLines = 1, overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (entry.header.isNotEmpty()) {
            AsyncImage(
                model = entry.header,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        Text(
            text = entry.title,
            style = MaterialTheme.typography.headlineSmall,
            fontFamily = robotoMonoFontFamily
        )

        if (entry.subtitle.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = entry.subtitle,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = rubikFontFamily
            )
        }
    }

    CobaltDivider(padding = 0.dp)
}

//

@Composable
private fun UpcomingEvents(
    events: ImmutableList<NewsEvent>
) {
    Text(
        text = "Upcoming events".uppercase(),
        style = MaterialTheme.typography.labelMedium,
        modifier = Modifier.padding(16.dp)
    )

    CobaltDivider()

    LazyRow(contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
        items(events, key = NewsEvent::id) { event ->
            Text(text = event.title)
        }
    }

    CobaltDivider(padding = 0.dp)
}

@Composable
private fun ActivityFeedPortal(
    feedEntry: ActivityFeedEntry
) {
    when (feedEntry) {
        is ActivityFeedEntry.PlayedForFirstTime -> {
            PlayedForFirstTimeEntry(feedEntry)
        }

        is ActivityFeedEntry.NewAchievements -> {
            NewAchievementsEntry(feedEntry)
        }

        is ActivityFeedEntry.ReceivedNewGame -> {
            ReceivedNewGameEntry(feedEntry)
        }

        else -> {
            Text(text = feedEntry.toString())
        }
    }

    CobaltDivider(padding = 0.dp)
}
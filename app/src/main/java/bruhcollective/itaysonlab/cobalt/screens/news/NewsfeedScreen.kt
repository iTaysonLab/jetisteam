package bruhcollective.itaysonlab.cobalt.screens.news

import android.text.format.DateUtils
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.cobalt.news.NewsfeedComponent
import bruhcollective.itaysonlab.cobalt.news.paging.NewsfeedPagingItem
import bruhcollective.itaysonlab.cobalt.screens.news.entries.AddedToWishlistEntry
import bruhcollective.itaysonlab.cobalt.screens.news.entries.NewAchievementsEntry
import bruhcollective.itaysonlab.cobalt.screens.news.entries.PlayedForFirstTimeEntry
import bruhcollective.itaysonlab.cobalt.screens.news.entries.PostedStatusEntry
import bruhcollective.itaysonlab.cobalt.screens.news.entries.ReceivedNewGameEntry
import bruhcollective.itaysonlab.cobalt.screens.news.entries.ScreenshotPostedEntry
import bruhcollective.itaysonlab.cobalt.screens.news.entries.ScreenshotsPostedEntry
import bruhcollective.itaysonlab.cobalt.ui.ScrollToTopHandler
import bruhcollective.itaysonlab.cobalt.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.cobalt.ui.components.ExceptionPage
import bruhcollective.itaysonlab.ksteam.models.news.NewsEvent
import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry
import coil.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NewsfeedScreen(
    component: NewsfeedComponent,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "News")
                }, colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }, contentWindowInsets = EmptyWindowInsets
    ) { innerPadding ->
        NewsfeedScreenContent(
            component = component,
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        )
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
internal fun NewsfeedScreenContent(
    component: NewsfeedComponent,
    modifier: Modifier
) {
    val loadState by component.state.subscribeAsState()

    val upcomingItems by component.upcomingItems.subscribeAsState()
    val items by component.items.subscribeAsState()
    val isLoading by component.isLoading.subscribeAsState()
    val canLoadMore by component.canLoadMore.subscribeAsState()

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    ScrollToTopHandler {
        scope.launch {
            listState.animateScrollToItem(0)
        }
    }

    when (val s = loadState) {
        CobaltScreenResult.Loading -> {
            LaunchedEffect(Unit) {
                component.dispatchLoad()
            }

            Box(
                modifier = modifier,
                contentAlignment = Alignment.Center
            ) {
                LoadingIndicator()
            }
        }

        is CobaltScreenResult.Error -> {
            ExceptionPage(
                result = s,
                modifier = modifier.padding(16.dp)
            )
        }

        CobaltScreenResult.Loaded -> {
            LazyColumn(
                state = listState,
                modifier = modifier,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (upcomingItems.isNotEmpty()) {
                    item {
                        Text(
                            stringResource(R.string.news_feed_upcoming),
                            modifier = Modifier.padding(horizontal = 16.dp),
                            style = MaterialTheme.typography.labelLarge
                        )
                    }

                    items(
                        items = upcomingItems,
                        key = NewsfeedPagingItem::id,
                        contentType = NewsfeedPagingItem::contentType
                    ) { event ->
                        EntryPortal(event, modifier = Modifier.fillParentMaxWidth())
                    }
                }

                items(
                    items = items,
                    key = NewsfeedPagingItem::id,
                    contentType = NewsfeedPagingItem::contentType
                ) { event ->
                    EntryPortal(event, modifier = Modifier.fillParentMaxWidth())
                }

                if (canLoadMore) {
                    item {
                        LaunchedEffect(Unit) {
                            component.dispatchLoad()
                        }

                        Box(
                            modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            LoadingIndicator()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun EntryPortal(entry: NewsfeedPagingItem, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)
        ), modifier = modifier, shape = MaterialTheme.shapes.large
    ) {
        when (entry) {
            is NewsfeedPagingItem.SteamNewsPost -> {
                NewsEventPortal(entry.item)
            }

            is NewsfeedPagingItem.UserActivityUpdate -> {
                ActivityFeedPortal(entry.item)
            }
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
                model = entry.relatedApp?.assets?.icon ?: entry.clanSummary?.avatarMedium,
                contentDescription = null,
                modifier = Modifier.size(38.dp),
                placeholder = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                error = ColorPainter(MaterialTheme.colorScheme.surfaceVariant),
                contentScale = ContentScale.FillBounds
            )

            Column {
                Text(
                    text = entry.relatedApp?.name ?: entry.clanSummary?.name.orEmpty(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

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
            style = MaterialTheme.typography.headlineSmall
        )

        if (entry.subtitle.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = entry.subtitle,
                style = MaterialTheme.typography.bodyMedium,
            )
        } else {

        }
    }
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

        is ActivityFeedEntry.AddedToWishlist -> {
            AddedToWishlistEntry(feedEntry)
        }

        is ActivityFeedEntry.ScreenshotPosted -> {
            ScreenshotPostedEntry(feedEntry)
        }

        is ActivityFeedEntry.ScreenshotsPosted -> {
            ScreenshotsPostedEntry(feedEntry)
        }

        is ActivityFeedEntry.PostedStatus -> {
            PostedStatusEntry(feedEntry)
        }

        else -> {
            Text(text = feedEntry.toString())
        }
    }
}
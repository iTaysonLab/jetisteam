package bruhcollective.itaysonlab.cobalt.news.discover

import bruhcollective.itaysonlab.ksteam.models.news.NewsEvent
import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList

interface DiscoverComponent {
    val state: Value<DiscoverState>
    val items: Value<ImmutableList<DiscoverItem>>
    val canLoadMore: Value<Boolean>

    fun dispatchInitialLoad()
    fun dispatchPagingLoad()

    enum class DiscoverState {
        Idle, Loading, Loaded, Error
    }

    sealed class DiscoverItem (
        val id: String,
        val contentType: Int,
        val date: Int
    ) {
        class UpcomingEvents(
            val events: ImmutableList<NewsEvent>
        ): DiscoverItem(id = "ue_${events.hashCode()}", contentType = -1, date = 0)

        class ActivityFeed(
            val entry: ActivityFeedEntry
        ): DiscoverItem(id = entry.id, contentType = entry.toContentTypeInt(), date = entry.date)

        class NewsPost(
            val entry: NewsEvent
        ): DiscoverItem(id = entry.id, contentType = -2, date = entry.publishedAt)
    }
}

private fun ActivityFeedEntry.toContentTypeInt(): Int {
    return when (this) {
        is ActivityFeedEntry.AddedToWishlist -> 0
        is ActivityFeedEntry.NewAchievements -> 1
        is ActivityFeedEntry.PlayedForFirstTime -> 2
        is ActivityFeedEntry.ReceivedNewGame -> 3
        is ActivityFeedEntry.UnknownEvent -> 4
        is ActivityFeedEntry.ScreenshotPosted -> 5
        is ActivityFeedEntry.ScreenshotsPosted -> 6
        is ActivityFeedEntry.PostedStatus -> 7
    }
}

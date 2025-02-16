package bruhcollective.itaysonlab.cobalt.news.paging

import bruhcollective.itaysonlab.ksteam.models.news.NewsEvent
import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry

/**
 * Defines an item that is shown on the newsfeed.
 */
sealed interface NewsfeedPagingItem {
    /**
     * Unique ID of an item.
     */
    val id: String

    /**
     * Content type of an item.
     */
    val contentType: Int

    /**
     * Item date, to be shown in shared UI and in sorting algorithms.
     */
    val date: Long

    /**
     * Represents an arbitrary news item that points to a Steam News post.
     */
    data class SteamNewsPost (
        val item: NewsEvent
    ): NewsfeedPagingItem {
        override val id: String get() = item.id
        override val contentType: Int = -1
        override val date: Long get() = item.publishedAt.toLong()
    }

    /**
     * Represents an user activity item.
     */
    data class UserActivityUpdate (
        val item: ActivityFeedEntry
    ): NewsfeedPagingItem {
        override val id: String get() = item.id
        override val date: Long get() = item.date.toLong()

        override val contentType: Int = when (item) {
            is ActivityFeedEntry.AddedToWishlist -> 0
            is ActivityFeedEntry.NewAchievements -> 1
            is ActivityFeedEntry.PlayedForFirstTime -> 2
            is ActivityFeedEntry.ReceivedNewGame -> 3
            is ActivityFeedEntry.UnknownEvent -> 4
            is ActivityFeedEntry.ScreenshotPosted -> 5
            is ActivityFeedEntry.ScreenshotsPosted -> 6
            is ActivityFeedEntry.PostedStatus -> 7
            is ActivityFeedEntry.FriendAdded -> 8
        }
    }
}
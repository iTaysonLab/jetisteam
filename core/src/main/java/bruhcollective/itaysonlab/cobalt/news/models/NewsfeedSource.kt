@file:UseSerializers(AppIdSerializer::class, SteamIdSerializer::class)

package bruhcollective.itaysonlab.cobalt.news.models

import bruhcollective.itaysonlab.ksteam.handlers.UserNews
import bruhcollective.itaysonlab.ksteam.models.AppId
import bruhcollective.itaysonlab.ksteam.models.SteamId
import bruhcollective.itaysonlab.ksteam.models.news.NewsAppType
import bruhcollective.itaysonlab.ksteam.models.news.NewsEventType
import bruhcollective.itaysonlab.ksteam.serialization.AppIdSerializer
import bruhcollective.itaysonlab.ksteam.serialization.SteamIdSerializer
import kotlinx.serialization.UseSerializers

/**
 * Defines a newsfeed "source".
 */
data class NewsfeedSource(
    /**
     * Contextual source - a clan or application.
     */
    val context: NewsfeedContext = NewsfeedContext.None,

    /**
     * Should include UserNews? (Activity updates)
     */
    val includeUserNews: Boolean = true,

    /**
     * Should include News? (Game updates, sales and other)
     */
    val includeNews: Boolean = true,

    /**
     * Filter content types for UserNews
     */
    val userNewsContentTypes: Int = UserNews.UserNewsFilterScenario.FriendActivity,

    /**
     *
     */
    val newsContentTypes: List<NewsEventType> = NewsEventType.Collections.Everything,

    /**
     *
     */
    val newsCollectionId: String? = null,

    /**
     *
     */
    val newsAppTypes: List<NewsAppType> = NewsAppType.Default,
) {
    sealed interface NewsfeedContext {
        /**
         * Filter by application.
         */
        data class Application(val id: AppId) : NewsfeedContext

        /**
         * Filter by clan ID. UserNews will explicitly be disabled.
         */
        data class Clan(val id: SteamId) : NewsfeedContext

        /**
         * Filter by upcoming events. UserNews will explicitly be disabled.
         */
        data object Upcoming: NewsfeedContext

        /**
         * Don't filter by anything.
         */
        data object None : NewsfeedContext
    }
}
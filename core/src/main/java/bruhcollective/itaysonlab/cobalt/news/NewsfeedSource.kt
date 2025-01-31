package bruhcollective.itaysonlab.cobalt.news

import bruhcollective.itaysonlab.ksteam.models.AppId
import bruhcollective.itaysonlab.ksteam.models.SteamId
import bruhcollective.itaysonlab.ksteam.models.news.NewsEventType

/**
 * Defines a newsfeed "source".
 */
data class NewsfeedSource (
    /**
     * Contextual source - a clan or application.
     */
    val context: NewsfeedContext,

    /**
     * Should include UserNews? (Activity updates)
     */
    val includeUserNews: Boolean,

    /**
     * Should include News? (Game updates, sales and other)
     */
    val includeNews: Boolean,

    /**
     * Filter content types for UserNews
     */
    val userNewsContentTypes: Int,

    /**
     *
     */
    val newsContentTypes: List<NewsEventType>
) {
    sealed interface NewsfeedContext {
        /**
         * Filter by application.
         */
        data class Application (val id: AppId): NewsfeedContext

        /**
         * Filter by clan ID. UserNews will explicitly be disabled.
         */
        data class Clan (val id: SteamId): NewsfeedContext

        /**
         * Don't filter by anything.
         */
        data object None: NewsfeedContext
    }
}
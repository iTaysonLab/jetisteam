package bruhcollective.itaysonlab.jetisteam.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Reviews(
    val success: Int,
    @SerialName("query_summary") val summary: ReviewQuerySummary?,
    val reviews: List<Review>
) {
    @Serializable
    class ReviewQuerySummary(
        @SerialName("num_reviews") val reviewCount: Int,
        @SerialName("review_score") val reviewScore: Int,
        @SerialName("review_score_desc") val reviewScoreDesc: String,
        @SerialName("total_positive") val positiveReviews: Int,
        @SerialName("total_negative") val negativeReviews: Int,
        @SerialName("total_reviews") val totalReviews: Int,
    )
}

@Serializable
class Review(
    @SerialName("recommendationid") val id: String,
    val author: ReviewAuthor,
    val review: String,
    @SerialName("timestamp_created") val createdAt: Long,
    @SerialName("timestamp_updated") val updatedAt: Long,
    @SerialName("voted_up") val positiveReview: Boolean,
    @SerialName("votes_up") val markedAsHelpful: Int,
    @SerialName("votes_funny") val markedAsFunny: Int,
    @SerialName("weighted_vote_score") val score: String,
    @SerialName("comment_count") val comments: Int,
    @SerialName("steam_purchase") val purchasedInSteam: Boolean,
    @SerialName("received_for_free") val markedAsFree: Boolean,
    @SerialName("written_during_early_access") val markedAsEarlyAccess: Boolean,
) {
    @Serializable
    class ReviewAuthor(
        val steamid: Long,
        @SerialName("num_games_owned") val ownedGames: Int,
        @SerialName("num_reviews") val reviewsWritten: Int,
        @SerialName("playtime_forever") val totalPlaytime: Int,
        @SerialName("playtime_last_two_weeks") val twoWeeksPlaytime: Int,
        @SerialName("playtime_at_review") val reviewPlaytime: Int,
        @SerialName("last_played") val lastLaunched: Long,
    ) {
        val steamId get() = SteamID(steamid)
    }
}
package bruhcollective.itaysonlab.jetisteam.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Reviews(
    val success: Int,
    @Json(name = "query_summary") val summary: ReviewQuerySummary?,
    val reviews: List<Review>
) {
    @JsonClass(generateAdapter = true)
    class ReviewQuerySummary(
        @Json(name = "num_reviews") val reviewCount: Int,
        @Json(name = "review_score") val reviewScore: Int,
        @Json(name = "review_score_desc") val reviewScoreDesc: String,
        @Json(name = "total_positive") val positiveReviews: Int,
        @Json(name = "total_negative") val negativeReviews: Int,
        @Json(name = "total_reviews") val totalReviews: Int,
    )
}

@JsonClass(generateAdapter = true)
class Review(
    @Json(name = "recommendationid") val id: String,
    val author: ReviewAuthor,
    val review: String,
    @Json(name = "timestamp_created") val createdAt: Long,
    @Json(name = "timestamp_updated") val updatedAt: Long,
    @Json(name = "voted_up") val positiveReview: Boolean,
    @Json(name = "votes_up") val markedAsHelpful: Int,
    @Json(name = "votes_funny") val markedAsFunny: Int,
    @Json(name = "weighted_vote_score") val score: String,
    @Json(name = "comment_count") val comments: Int,
    @Json(name = "steam_purchase") val purchasedInSteam: Boolean,
    @Json(name = "received_for_free") val markedAsFree: Boolean,
    @Json(name = "written_during_early_access") val markedAsEarlyAccess: Boolean,
) {
    @JsonClass(generateAdapter = true)
    class ReviewAuthor(
        val steamid: Long,
        @Json(name = "num_games_owned") val ownedGames: Int,
        @Json(name = "num_reviews") val reviewsWritten: Int,
        @Json(name = "playtime_forever") val totalPlaytime: Int,
        @Json(name = "playtime_last_two_weeks") val twoWeeksPlaytime: Int,
        @Json(name = "playtime_at_review") val reviewPlaytime: Int,
        @Json(name = "last_played") val lastLaunched: Long,
    ) {
        val steamId get() = SteamID(steamid)
    }
}
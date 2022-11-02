package bruhcollective.itaysonlab.jetisteam.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Reviews(
    val success: Int,
    @Json(name = "query_summary") val summary: ReviewQuerySummary,
    val reviews: List<Review>
) {
    @JsonClass(generateAdapter = true)
    class ReviewQuerySummary(
        @Json(name = "num_reviews") reviewCount: Int,
        @Json(name = "review_score") reviewScore: Int,
        @Json(name = "review_score") reviewScoreDesc: String,
        @Json(name = "total_positive") positiveReviews: Int,
        @Json(name = "total_negative") negativeReviews: Int,
        @Json(name = "total_reviews") totalReviews: Int,
    )
}

@JsonClass(generateAdapter = true)
class Review(
    @Json(name = "recommendationid") val id: String,
    val author: ReviewAuthor,
    val review: String,
    @Json(name = "timestamp_created") createdAt: Long,
    @Json(name = "timestamp_updated") updatedAt: Long,
    @Json(name = "voted_up") positiveReview: Boolean,
    @Json(name = "votes_up") markedAsHelpful: Int,
    @Json(name = "votes_funny") markedAsFunny: Int,
    @Json(name = "weighted_vote_score") score: String,
    @Json(name = "comment_count") comments: Int,
    @Json(name = "steam_purchase") purchasedInSteam: Boolean,
    @Json(name = "received_for_free") markedAsFree: Boolean,
    @Json(name = "written_during_early_access") markedAsEarlyAccess: Boolean,
) {
    @JsonClass(generateAdapter = true)
    class ReviewAuthor(
        val steamid: Long,
        @Json(name = "num_games_owned") ownedGames: Int,
        @Json(name = "num_reviews") reviewsWritten: Int,
        @Json(name = "playtime_forever") totalPlaytime: Int,
        @Json(name = "playtime_last_two_weeks") twoWeeksPlaytime: Int,
        @Json(name = "playtime_at_review") reviewPlaytime: Int,
        @Json(name = "last_played") lastLaunched: Long,
    ) {
        val steamId get() = SteamID(steamid)
    }
}
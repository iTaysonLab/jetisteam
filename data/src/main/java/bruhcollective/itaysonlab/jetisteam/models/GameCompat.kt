package bruhcollective.itaysonlab.jetisteam.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class GameCompatDetails(
    val success: Boolean,
    val data: GameCompatDetailsData?
)

@JsonClass(generateAdapter = true)
class GameCompatDetailsData(
    @Json(name = "friendsown") val friendsOwners: List<Owner>,
    @Json(name = "recommendations") val friendsRecommendations: Recommendations,
    @Json(name = "added_to_wishlist") val inWishlist: Boolean,
    @Json(name = "is_owned") val inLibrary: Boolean
) {
    @JsonClass(generateAdapter = true)
    class Owner(
        val steamid: Long,
        @Json(name = "playtime_twoweeks") twoWeeksPlaytime: Int,
        @Json(name = "playtime_total") totalPlaytime: Int,
    ) {
        val steamId get() = SteamID(steamid)
    }

    @JsonClass(generateAdapter = true)
    class Recommendations(
        @Json(name = "totalfriends") val friendsWithReviews: Int,
        val friends: List<FriendReview>
    ) {
        @JsonClass(generateAdapter = true)
        class FriendReview(
            val steamid: Long,
            val timestamp: Long,
            val recommendation: String,
            @Json(name = "commentcount") val comments: Int
        ) {
            val steamId get() = SteamID(steamid)
        }
    }
}
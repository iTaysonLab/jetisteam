package bruhcollective.itaysonlab.jetisteam.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GameCompatDetails(
    val success: Boolean,
    val data: GameCompatDetailsData?
)

@Serializable
class GameCompatDetailsData(
    @SerialName("friendsown") val friendsOwners: List<Owner>,
    @SerialName("recommendations") val friendsRecommendations: Recommendations,
    @SerialName("added_to_wishlist") val inWishlist: Boolean,
    @SerialName("is_owned") val inLibrary: Boolean
) {
    @Serializable
    class Owner(
        val steamid: Long,
        @SerialName("playtime_twoweeks") val twoWeeksPlaytime: Int,
        @SerialName("playtime_total") val totalPlaytime: Int,
    ) {
        val steamId get() = SteamID(steamid)
    }

    @Serializable
    class Recommendations(
        @SerialName("totalfriends") val friendsWithReviews: Int,
        val friends: List<FriendReview>?
    ) {
        @Serializable
        class FriendReview(
            val steamid: Long,
            val timestamp: Long,
            val recommendation: String,
            @SerialName("commentcount") val comments: Int
        ) {
            val steamId get() = SteamID(steamid)
        }
    }
}
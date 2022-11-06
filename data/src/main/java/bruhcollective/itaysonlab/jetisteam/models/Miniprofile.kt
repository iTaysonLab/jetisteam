package bruhcollective.itaysonlab.jetisteam.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Miniprofile (
    val level: Int,
    @Json(name = "avatar_url") val avatarUrl: String,
    @Json(name = "persona_name") val personaName: String,
    @Json(name = "favorite_badge") val favoriteBadge: MiniprofileBadge?,
    @Json(name = "in_game") val inGame: MiniprofileIngame?,
)

@JsonClass(generateAdapter = true)
class MiniprofileBadge(
    val name: String,
    val xp: String,
    val level: Int,
    val description: String,
    val icon: String
)

@JsonClass(generateAdapter = true)
class MiniprofileIngame(
    val name: String,
    val logo: String,
    @Json(name = "rich_presence") val richPresence: String
)

@JsonClass(generateAdapter = true)
class FriendApiResponse (
    @Json(name = "steamid") val steamId: Long,
    @Json(name = "avatarfull") val avatarUrl: String,
    @Json(name = "personaname") val personaName: String,
    @Json(name = "personastate") val personaState: Int,
    @Json(name = "profilestate") val profileState: Int,
    @Json(name = "lastlogoff") val lastLogoff: Long? = null,
    @Json(name = "gameid") val gameId: Long? = null,
    @Json(name = "gameextrainfo") val gameName: String? = null,
)
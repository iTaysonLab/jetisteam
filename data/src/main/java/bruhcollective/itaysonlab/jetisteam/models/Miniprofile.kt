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
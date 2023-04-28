package bruhcollective.itaysonlab.jetisteam.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class Miniprofile (
    val level: Int,
    @SerialName("avatar_url") val avatarUrl: String,
    @SerialName("persona_name") val personaName: String,
    @SerialName("favorite_badge") val favoriteBadge: MiniprofileBadge?,
    @SerialName("in_game") val inGame: MiniprofileIngame?,
)

@Serializable
class MiniprofileBadge(
    val name: String,
    val xp: String,
    val level: Int,
    val description: String,
    val icon: String
)

@Serializable
class MiniprofileIngame(
    val name: String,
    val logo: String,
    @SerialName("rich_presence") val richPresence: String
)
package bruhcollective.itaysonlab.jetisteam.models

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class Players(
    val players: List<Player>
)

@JsonClass(generateAdapter = true)
class Player(
    val steamid: String,
    val communityvisibilitystate: Int,
    val profilestate: Int,
    val personastate: Int,
    val personaname: String,
    val profileurl: String,
    val avatar: String,
    val avatarmedium: String,
    val avatarfull: String,
    val avatarhash: String,
    val lastlogoff: Long,
    val timecreated: Long,
    val loccountrycode: String?,
    val primaryclanid: String,
    val realname: String?,
    val gameextrainfo: String?,
    val gameid: Int?
) {
    val steamId get() = SteamID(steamid.toLong())
}
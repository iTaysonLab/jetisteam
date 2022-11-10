package bruhcollective.itaysonlab.jetisteam.mappers

import bruhcollective.itaysonlab.jetisteam.models.Player

class FriendProfile(
    val steamId: Long,
    val name: String,
    val avatarUrl: String,
    val status: FriendStatus,
    val playingGame: GameInfo?
) {
    constructor(player: Player): this(
        steamId = player.steamId.steamId,
        name = player.personaname,
        avatarUrl = player.avatarfull,
        status = player.personastate.toFriendStatus(player),
        playingGame = player.gameid?.let { GameInfo(it, player.gameextrainfo ?: "") },
    )

}

class GameInfo(val id: Long, val name: String)

sealed class FriendStatus {
    class Offline(val lastLogoff: Long) : FriendStatus()
    object Online : FriendStatus()
    object Busy : FriendStatus()
    object Away : FriendStatus()
    object Snooze : FriendStatus()
    object LookingToTrade : FriendStatus()
    object LookingToPlay : FriendStatus()
}

// https://partner.steamgames.com/doc/api/ISteamFriends#EPersonaState
fun Int.toFriendStatus(summary: Player) = when(this) {
    0 -> FriendStatus.Offline(summary.lastlogoff)
    1 -> FriendStatus.Online
    2 -> FriendStatus.Busy
    3 -> FriendStatus.Away
    4 -> FriendStatus.Snooze
    5 -> FriendStatus.LookingToTrade
    6 -> FriendStatus.LookingToPlay
    else -> throw IllegalArgumentException()
}
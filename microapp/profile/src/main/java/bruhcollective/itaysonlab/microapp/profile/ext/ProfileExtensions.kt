package bruhcollective.itaysonlab.microapp.profile.ext

import androidx.compose.ui.graphics.Color
import bruhcollective.itaysonlab.jetisteam.mappers.FriendStatus
import bruhcollective.itaysonlab.microapp.profile.R

fun FriendStatus.toSortingInt() = when(this) {
    FriendStatus.LookingToPlay -> 0
    FriendStatus.LookingToTrade -> 1
    FriendStatus.Online -> 2
    FriendStatus.Busy -> 3
    FriendStatus.Away -> 4
    FriendStatus.Snooze -> 5
    is FriendStatus.Offline -> 6
}

fun FriendStatus.toStringRes() = when (this) {
    FriendStatus.Online -> R.string.friends_status_online
    FriendStatus.Busy -> R.string.friends_status_busy
    FriendStatus.Away -> R.string.friends_status_away
    FriendStatus.Snooze -> R.string.friends_status_snooze
    FriendStatus.LookingToPlay -> R.string.friends_status_looking_to_play
    FriendStatus.LookingToTrade -> R.string.friends_status_looking_to_trade
    is FriendStatus.Offline -> R.string.friends_status_offline
}

enum class FriendGroups(val stringRes: Int, val color: Color) {
    OFFLINE(R.string.friends_group_offline, Color(0xFF898989)),
    ONLINE(R.string.friends_group_online, Color(0xFF57cbde)),
    PLAYING(R.string.friends_group_playing, Color(0xFFa3cf06))
}
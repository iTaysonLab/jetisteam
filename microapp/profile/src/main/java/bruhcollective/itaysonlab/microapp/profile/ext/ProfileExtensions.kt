package bruhcollective.itaysonlab.microapp.profile.ext

import bruhcollective.itaysonlab.ksteam.models.enums.EPersonaState
import bruhcollective.itaysonlab.microapp.profile.R

/*import androidx.compose.ui.graphics.Color
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

enum class FriendGroups(val stringRes: Int, val color: Color) {
    OFFLINE(R.string.friends_group_offline, Color(0xFF898989)),
    ONLINE(R.string.friends_group_online, Color(0xFF57cbde)),
    PLAYING(R.string.friends_group_playing, Color(0xFFa3cf06))
}*/

fun EPersonaState.toStringRes() = when (this) {
    EPersonaState.Online -> R.string.friends_status_online
    EPersonaState.Busy -> R.string.friends_status_busy
    EPersonaState.Away -> R.string.friends_status_away
    EPersonaState.Snooze -> R.string.friends_status_snooze
    EPersonaState.LookingToPlay -> R.string.friends_status_looking_to_play
    EPersonaState.LookingToTrade -> R.string.friends_status_looking_to_trade
    EPersonaState.Offline -> R.string.friends_status_offline
}
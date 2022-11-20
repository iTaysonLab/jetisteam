package bruhcollective.itaysonlab.microapp.profile.ui.screens.friends

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.mappers.FriendProfile
import bruhcollective.itaysonlab.jetisteam.mappers.FriendStatus
import bruhcollective.itaysonlab.microapp.profile.R
import bruhcollective.itaysonlab.microapp.profile.ext.FriendGroups
import bruhcollective.itaysonlab.microapp.profile.ext.toLastSeenDate
import bruhcollective.itaysonlab.microapp.profile.ext.toStringRes
import coil.compose.AsyncImage

@ExperimentalComposeUiApi
@Composable
internal fun FriendListItem(
    group: FriendGroups,
    friend: FriendProfile,
    onFriendClick: (Long) -> Unit,
) {
    Box(modifier = Modifier
        .padding(horizontal = 16.dp)
        .clip(CardDefaults.shape)
        .clickable { onFriendClick(friend.steamId) }
        .padding(4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            AsyncImage(
                model = friend.avatarUrl,
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .border(2.dp, group.color, CardDefaults.shape)
                    .clip(CardDefaults.shape),
                contentScale = ContentScale.Crop,
            )
            Column(modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()) {
                Text(
                    text = friend.name,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    fontWeight = FontWeight.SemiBold,
                )

                val statusText = when (group) {
                    FriendGroups.PLAYING -> friend.playingGame!!.name
                    FriendGroups.ONLINE -> stringResource(id = friend.status.toStringRes())
                    FriendGroups.OFFLINE -> stringResource(
                        id = R.string.friends_offline_last_seen,
                        (friend.status as FriendStatus.Offline).lastLogoff.toLastSeenDate()
                    )
                }

                Text(
                    text = statusText,
                    fontSize = 14.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                )
            }
        }
    }
}
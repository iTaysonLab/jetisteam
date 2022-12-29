package bruhcollective.itaysonlab.microapp.steam_wrapped.ui.blocks

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.models.Player
import bruhcollective.itaysonlab.jetisteam.uikit.LocalSteamTheme
import coil.compose.AsyncImage

@Composable
fun ReplayHeader(
    backgroundUrl: String?,
    avatarUrl: String?,
    avatarFrameUrl: String?,
    profile: Player,
) {
    val theme = LocalSteamTheme.current

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        AsyncImage(
            model = backgroundUrl,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(
                        Brush.verticalGradient(
                            colorStops = arrayOf(
                                0f to Color.Transparent,
                                1f to theme.gradientBackground.copy(alpha = 1f),
                            )
                        )
                    )
                },
            contentScale = ContentScale.Crop
        )

        Column(Modifier.padding(bottom = 8.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            Spacer(modifier = Modifier.height(136.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (avatarFrameUrl != null) {
                    Box {
                        AsyncImage(
                            model = avatarUrl,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )

                        AsyncImage(
                            model = avatarFrameUrl,
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .scale(1.22f)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))
                } else {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .size(32.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = profile.personaname,
                    fontSize = 16.sp,
                    color = Color.White,
                )
            }

            Text(
                text = "Steam Replay 2022",
                fontSize = 21.sp,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                color = Color.White,
            )

            /*val friendStatus = remember(profile) {
                profile.personastate.toFriendStatus(profile)
            }

            Text(
                text = when {
                    profile.gameid != null -> "Playing ${profile.gameextrainfo.orEmpty()}"
                    friendStatus is FriendStatus.Offline -> stringResource(id = R.string.friends_offline_last_seen, friendStatus.lastLogoff.toLastSeenDate())
                    else -> stringResource(id = friendStatus.toStringRes())
                },
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.White.copy(alpha = 0.7f),
            )*/
        }
    }
}
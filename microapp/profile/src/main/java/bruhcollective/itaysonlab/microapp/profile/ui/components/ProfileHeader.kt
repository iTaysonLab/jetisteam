package bruhcollective.itaysonlab.microapp.profile.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.uikit.LocalSteamTheme
import bruhcollective.itaysonlab.ksteam.models.enums.EFriendRelationship
import bruhcollective.itaysonlab.ksteam.models.enums.EPersonaState
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import bruhcollective.itaysonlab.microapp.profile.R
import bruhcollective.itaysonlab.microapp.profile.ext.toLastSeenDate
import bruhcollective.itaysonlab.microapp.profile.ext.toStringRes
import coil.compose.AsyncImage

@OptIn(ExperimentalComposeUiApi::class)
@Composable
internal fun ProfileHeader(
    backgroundUrl: String?,
    avatarUrl: String?,
    avatarFrameUrl: String?,
    profileName: String,
    lastSeen: Persona.LastSeen,
    relationship: EFriendRelationship,
    personaState: EPersonaState,
    onLibraryClick: () -> Unit,
    onFriendsClick: () -> Unit
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

            if (avatarFrameUrl != null) {
                Box {
                    AsyncImage(
                        model = avatarUrl,
                        contentDescription = null,
                        modifier = Modifier.size(72.dp)
                    )

                    AsyncImage(
                        model = avatarFrameUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(72.dp)
                            .scale(1.22f)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            } else {
                AsyncImage(
                    model = avatarUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .size(72.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            Text(
                text = profileName,
                fontSize = 21.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.White,
            )

            Text(
                text = when (personaState) {
                    EPersonaState.Offline -> stringResource(id = R.string.friends_offline_last_seen, lastSeen.lastLogOff.toLastSeenDate())
                    else -> stringResource(id = personaState.toStringRes())
                },
                fontSize = 16.sp,
                modifier = Modifier.padding(horizontal = 16.dp),
                color = Color.White.copy(alpha = 0.7f),
            )

            /*if (summary != null) {
                Spacer(modifier = Modifier.height(12.dp))

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    modifier = Modifier.fillMaxWidth().height(40.dp),
                ) {
                    item {
                        Button(onClick = onFriendsClick, colors = ButtonDefaults.buttonColors(
                            containerColor = theme.btnBackground.copy(alpha = 0.5f),
                            contentColor = Color.White
                        ), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                            Icon(imageVector = Icons.Rounded.Person, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${summary.friendCount} friends")
                        }
                    }

                    item {
                        Button(onClick = onLibraryClick, colors = ButtonDefaults.buttonColors(
                            containerColor = theme.btnBackground.copy(alpha = 0.5f),
                            contentColor = Color.White
                        ), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                            Icon(imageVector = Icons.Rounded.Gamepad, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${summary.ownedGames} games")
                        }
                    }
                }
            }*/
        }
    }
}
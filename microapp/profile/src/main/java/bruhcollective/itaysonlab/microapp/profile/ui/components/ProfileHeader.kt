package bruhcollective.itaysonlab.microapp.profile.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.mappers.ProfileSummary
import bruhcollective.itaysonlab.microapp.profile.ui.LocalSteamTheme
import coil.compose.AsyncImage

@Composable
internal fun ProfileHeader(
    backgroundUrl: String?,
    avatarUrl: String?,
    avatarFrameUrl: String?,
    personaName: String,
    summary: ProfileSummary?
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

        Column(Modifier.padding(bottom = 8.dp)) {
            Spacer(modifier = Modifier.height(150.dp))

            Box(
                Modifier
                    .padding(start = 4.dp, bottom = 16.dp)
                    .padding(horizontal = 16.dp)) {
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

            Text(text = personaName, fontSize = 21.sp, modifier = Modifier.padding(horizontal = 16.dp), color = Color.White)

            Spacer(modifier = Modifier.height(12.dp))

            if (summary != null) {
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp), contentPadding = PaddingValues(horizontal = 16.dp), modifier = Modifier.fillMaxWidth().height(40.dp)) {
                    item {
                        Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(
                            containerColor = theme.btnBackground,
                            contentColor = Color.White
                        )) {
                            Text(text = "Edit profile")
                        }
                    }

                    item {
                        Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(
                            containerColor = theme.btnBackground.copy(alpha = 0.5f),
                            contentColor = Color.White
                        ), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                            Icon(imageVector = Icons.Rounded.Person, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${summary.friendCount} friends")
                        }
                    }

                    item {
                        Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(
                            containerColor = theme.btnBackground.copy(alpha = 0.5f),
                            contentColor = Color.White
                        ), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                            Icon(imageVector = Icons.Rounded.Gamepad, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "${summary.ownedGames} games")
                        }
                    }

                    item {
                        Button(onClick = { /*TODO*/ }, colors = ButtonDefaults.buttonColors(
                            containerColor = theme.btnBackground.copy(alpha = 0.5f),
                            contentColor = Color.White
                        ), contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)) {
                            Icon(imageVector = Icons.Rounded.Wallet, contentDescription = null)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = summary.walletBalance)
                        }
                    }
                }
            }
        }
    }
}
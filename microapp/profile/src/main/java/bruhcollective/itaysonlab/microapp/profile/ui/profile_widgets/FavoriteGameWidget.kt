package bruhcollective.itaysonlab.microapp.profile.ui.profile_widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.ext.roundUpTo
import bruhcollective.itaysonlab.ksteam.models.apps.libraryHeader
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileWidget
import bruhcollective.itaysonlab.microapp.profile.R
import coil.compose.AsyncImage

@Composable
internal fun FavoriteGameWidget(
    widget: ProfileWidget.FavoriteGame
) {
    val formattedHours = remember(widget.playedSeconds) {
        (widget.playedSeconds / 60f).roundUpTo(1)
    }

    val progress = remember(
        widget.achievementProgress.currentAchivements,
        widget.achievementProgress.totalAchievements
    ) {
        if (widget.achievementProgress.totalAchievements > 0) {
            (widget.achievementProgress.currentAchivements.toFloat() / widget.achievementProgress.totalAchievements.toFloat()).coerceIn(
                0f..1f
            )
        } else {
            0f
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        AsyncImage(
            model = widget.app.libraryHeader.url,
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .drawWithContent {
                    drawContent()
                    drawRect(Color.Black.copy(alpha = 0.5f))
                },
            contentScale = ContentScale.Crop
        )

        Column(Modifier.fillMaxWidth()) {
            Row(
                Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Rounded.Favorite, contentDescription = null)
                Text(text = stringResource(id = R.string.showcase_favorite_game))
            }

            Text(
                text = widget.app.name,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = pluralStringResource(
                    id = R.plurals.profile_hours_played,
                    count = formattedHours.toInt(),
                    formattedHours
                ),
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = progress,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .clip(CircleShape),
                trackColor = Color.White.copy(alpha = 0.5f),
                color = Color.White
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                "${widget.achievementProgress.currentAchivements} of ${widget.achievementProgress.totalAchievements} achievements",
                modifier = Modifier.alpha(0.75f).padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            /*Row(Modifier.height(IntrinsicSize.Min)) {
                AsyncImage(
                    model = widget.app.libraryEntry.url,
                    contentDescription = null,
                    modifier = Modifier
                        .width(120.dp)
                        .height(200.dp)
                )

                Column(Modifier.fillMaxWidth()) {
                    Text(text = widget.playedSeconds.toString())
                }
            }*/
        }
    }
}
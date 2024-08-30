package bruhcollective.itaysonlab.cobalt.profile.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.ArrowForwardIos
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.R
import bruhcollective.itaysonlab.cobalt.ext.roundUpTo
import bruhcollective.itaysonlab.ksteam.models.apps.libraryHeader
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileWidget
import coil.compose.AsyncImage

@Composable
fun FavoriteGameProfileWidget(
    widget: ProfileWidget.FavoriteGame
) {
    val formattedHours = remember(widget.playedSeconds) {
        (widget.playedSeconds / 60f).roundUpTo(1).toInt()
    }

    Box(modifier = Modifier.height(IntrinsicSize.Min)) {
        AsyncImage(
            model = widget.app.libraryHeader,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.75f))
        )

        DefaultPwCard(
            title = stringResource(id = R.string.profile_widget_favorite_game)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(text = widget.app.name)

                if (widget.playedSeconds > 0) {
                    Text(
                        text = pluralStringResource(
                            id = R.plurals.profile_hours_played,
                            count = formattedHours,
                            formattedHours
                        ), color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            if (widget.achievementProgress.totalAchievements != 0) {
                Divider(
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.25f),
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier) {
                    Text(
                        stringResource(id = R.string.profile_widget_favorite_game_achievements, widget.achievementProgress.currentAchievements, widget.achievementProgress.totalAchievements),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.weight(1f)
                    )

                    Icon(
                        imageVector = Icons.Sharp.ArrowForwardIos,
                        contentDescription = null,
                        modifier = Modifier.size(12.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
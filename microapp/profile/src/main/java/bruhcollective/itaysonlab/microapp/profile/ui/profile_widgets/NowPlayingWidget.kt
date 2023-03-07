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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayCircleOutline
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.ksteam.models.apps.libraryHeader
import bruhcollective.itaysonlab.microapp.profile.R
import bruhcollective.itaysonlab.microapp.profile.ui.ProfileScreenViewModel
import coil.compose.AsyncImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun NowPlayingWidget(
    richPresence: ProfileScreenViewModel.RichPresence
) {
    Card(
        onClick = { /*TODO*/ },
        modifier = Modifier.padding(horizontal = 16.dp),
        shape = MaterialTheme.shapes.large,
        colors = CardDefaults.cardColors(
            contentColor = Color.White
        )
    ) {
        Box(
            Modifier
                .fillMaxWidth()
                .height(IntrinsicSize.Min)
        ) {
            AsyncImage(
                model = richPresence.appSummary.libraryHeader.url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        drawContent()
                        drawRect(Color.Black.copy(alpha = 0.5f))
                    },
                contentScale = ContentScale.Crop
            )

            Column(
                Modifier
                    .fillMaxWidth()
            ) {
                Row(
                    Modifier
                        .padding(16.dp)
                        .fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Rounded.PlayCircleOutline, contentDescription = null)
                    Text(text = stringResource(id = R.string.widget_now_playing))
                }

                Column(Modifier.padding(horizontal = 16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(text = richPresence.appSummary.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold)

                    if (richPresence.formattedString.isNotEmpty()) {
                        Text(text = richPresence.formattedString)
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (richPresence.numbersInGame > 0) {
                    Divider(color = Color.White.copy(alpha = 0.25f))

                    Row(
                        Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                    ) {
                        Text(text = stringResource(id = R.string.widget_now_playing_group_size, richPresence.numbersInGame))
                    }
                }
            }
        }
    }
}
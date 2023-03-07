package bruhcollective.itaysonlab.microapp.profile.ui.profile_widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GridView
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.ksteam.models.apps.capsuleSmall
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileWidget
import bruhcollective.itaysonlab.microapp.profile.R
import coil.compose.AsyncImage

@Composable
internal fun GameCollectorWidget(
    widget: ProfileWidget.GameCollector
) {
    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(imageVector = Icons.Rounded.GridView, contentDescription = null)
            Text(text = stringResource(id = R.string.showcase_collector_game))
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(widget.featuredApps) { app ->
                AsyncImage(
                    model = app.capsuleSmall.url,
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        .clickable {  }
                        .wrapContentSize()
                        .height(70.dp)
                )
            }
        }

        Text(
            text = "${widget.ownedGamesCount} games and DLCs owned",
            color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp,
            modifier = Modifier.padding(16.dp)
        )
    }
}
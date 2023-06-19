package bruhcollective.itaysonlab.jetisteam.profile.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.ui.font.rubikFontFamily
import bruhcollective.itaysonlab.ksteam.models.apps.capsuleSmall
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileWidget
import coil.compose.AsyncImage

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun GameCollectorProfileWidget(
    widget: ProfileWidget.GameCollector
) {
    FlowRow(
        maxItemsInEachRow = 4,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.padding(vertical = 8.dp)
    ) {
        widget.featuredApps.forEach { app ->
            AsyncImage(
                model = app.capsuleSmall,
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.weight(1f)
            )
        }
    }

    Text(
        text = "${widget.ownedGamesCount} games and DLCs owned",
        color = MaterialTheme.colorScheme.onSurface,
        style = MaterialTheme.typography.labelLarge,
        fontFamily = rubikFontFamily
    )
}
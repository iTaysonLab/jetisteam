package bruhcollective.itaysonlab.microapp.profile.ui.components.slots

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.util.CdnUrlUtil
import bruhcollective.itaysonlab.microapp.profile.ui.Game
import coil.compose.AsyncImage

@Composable
internal fun GameCollector(
    getGameSize: () -> Int,
    games: List<Game>,
    onGameClick: (Int) -> Unit
) {
    val ownedGames = remember(games) { getGameSize() }

    LazyRow(
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(games) { game ->
            AsyncImage(
                model = remember(game.appid) {
                    CdnUrlUtil.buildAppUrl(game.appid!!, "capsule_231x87.jpg")
                },
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .clickable { onGameClick(game.appid!!) }
                    .wrapContentSize()
                    .height(70.dp)
            )
        }
    }

    Column(
        Modifier
            .background(Color.Black.copy(alpha = 0.15f))
            .padding(horizontal = 16.dp, vertical = 12.dp)
            .fillMaxWidth()
    ) {
        Text(
            text = "$ownedGames games and DLCs owned",
            color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp
        )
    }
}
package bruhcollective.itaysonlab.microapp.profile.ui.components.slots

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import bruhcollective.itaysonlab.jetisteam.util.CdnUrlUtil
import bruhcollective.itaysonlab.microapp.profile.ui.GameToAchievements
import bruhcollective.itaysonlab.microapp.profile.ui.LocalSteamTheme
import coil.compose.AsyncImage

@Composable
internal fun FavoriteGame(
    id: Int,
    getData: (Int) -> GameToAchievements,
    onGameClick: (Int) -> Unit,
) {
    val completedColor = LocalSteamTheme.current.gradientShowcaseHeaderLeft

    val content = remember(id) {
        val data = getData(id)

        FavoriteGameModel(
            name = data.first.name!!,
            playtimeForever = data.first.playtime_forever,
            achievementPercentage = data.second.percentage!! / 100f,
            achievementString = "${data.second.unlocked} of ${data.second.total}",
            hasAchievements = data.second.total != null && data.second.total != 0
        )
    }

    Column(
        Modifier
            .clickable { onGameClick(id) }
            .fillMaxWidth()
            .padding(16.dp)) {
        AsyncImage(
            model = remember(id) {
                CdnUrlUtil.buildAppUrl(id, "capsule_616x353.jpg")
            },
            contentDescription = null,
            modifier = Modifier.height(96.dp),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(text = content.name, color = Color.White, fontSize = 21.sp)

        if (content.playtimeForever != null && content.playtimeForever != 0) {
            Text(text = remember(content.playtimeForever) {
                "${content.playtimeForever / 60} hours played"
            }, color = Color.White.copy(alpha = 0.7f), fontSize = 16.sp)
        }
    }

    if (content.hasAchievements) {
        Column(
            Modifier
                .drawBehind {
                    drawRect(Color.Black.copy(alpha = 0.15f))
                    drawRect(completedColor, size = size.copy(width = size.width * content.achievementPercentage))
                }
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth()) {
            Text(text = "Achievement Progress", color = Color.White, fontSize = 16.sp)
            Text(text = content.achievementString, color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)
        }
    }
}

private class FavoriteGameModel(
    val name: String,
    val playtimeForever: Int?,
    val achievementPercentage: Float,
    val achievementString: String,
    val hasAchievements: Boolean
)
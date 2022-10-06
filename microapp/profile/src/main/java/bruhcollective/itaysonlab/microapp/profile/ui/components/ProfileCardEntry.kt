package bruhcollective.itaysonlab.microapp.profile.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.jetisteam.mappers.ProfileCustomizationEntry
import bruhcollective.itaysonlab.microapp.profile.R
import bruhcollective.itaysonlab.microapp.profile.ui.Game
import bruhcollective.itaysonlab.microapp.profile.ui.GameToAchievements
import bruhcollective.itaysonlab.microapp.profile.ui.LocalSteamTheme
import bruhcollective.itaysonlab.microapp.profile.ui.components.slots.FavoriteGame
import bruhcollective.itaysonlab.microapp.profile.ui.components.slots.GameCollector
import steam.player.EProfileCustomizationType

@Composable
internal fun ProfileCardEntry(
    entry: ProfileCustomizationEntry,
    getGameSize: () -> Int,
    getGame: (Int) -> Game,
    getGameWithAchievements: (Int) -> GameToAchievements,
    onGameClick: (Int) -> Unit
) {
    val name = remember(entry.customizationType) {
        when (entry.customizationType) {
            EProfileCustomizationType.k_EProfileCustomizationTypeRareAchievementShowcase -> R.string.showcase_achievements_rarest
            EProfileCustomizationType.k_EProfileCustomizationTypeGameCollector -> R.string.showcase_collector_game
            EProfileCustomizationType.k_EProfileCustomizationTypeItemShowcase -> R.string.showcase_items
            EProfileCustomizationType.k_EProfileCustomizationTypeTradeShowcase -> R.string.showcase_items_trade
            EProfileCustomizationType.k_EProfileCustomizationTypeBadges -> R.string.showcase_collector_badge
            EProfileCustomizationType.k_EProfileCustomizationTypeFavoriteGame -> R.string.showcase_favorite_game
            EProfileCustomizationType.k_EProfileCustomizationTypeScreenshotShowcase -> R.string.showcase_screenshots
            EProfileCustomizationType.k_EProfileCustomizationTypeCustomText -> R.string.showcase_info
            EProfileCustomizationType.k_EProfileCustomizationTypeFavoriteGroup -> R.string.showcase_favorite_group
            EProfileCustomizationType.k_EProfileCustomizationTypeWorkshopItem -> R.string.showcase_achievements_rarest
            EProfileCustomizationType.k_EProfileCustomizationTypeMyWorkshop -> R.string.showcase_workshop
            EProfileCustomizationType.k_EProfileCustomizationTypeGuides -> R.string.showcase_favorite_guide
            EProfileCustomizationType.k_EProfileCustomizationTypeAchievements -> R.string.showcase_achievements
            EProfileCustomizationType.k_EProfileCustomizationTypeSalien -> R.string.showcase_salien
            else -> R.string.showcase_unknown
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color.Black.copy(alpha = 0.2f))
    ) {

        Text(text = stringResource(id = name),
            color = Color.White,
            modifier = Modifier
                .background(LocalSteamTheme.current.colorShowcaseHeader)
                .padding(16.dp)
                .fillMaxWidth()
        )

        when (entry.customizationType) {
            EProfileCustomizationType.k_EProfileCustomizationTypeFavoriteGame -> FavoriteGame(remember(entry) { entry.slots.first().appId }, getGameWithAchievements, onGameClick)
            EProfileCustomizationType.k_EProfileCustomizationTypeGameCollector -> GameCollector(getGameSize, remember(entry) { entry.slots.map { slot -> getGame(slot.appId) } }, onGameClick)
            else -> Text("Unsupported!")
        }
    }
}
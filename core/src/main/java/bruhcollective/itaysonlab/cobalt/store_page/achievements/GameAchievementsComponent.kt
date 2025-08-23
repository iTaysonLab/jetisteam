package bruhcollective.itaysonlab.cobalt.store_page.achievements

import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.ksteam.models.news.usernews.ActivityFeedEntry
import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList

interface GameAchievementsComponent {
    val screenResult: Value<CobaltScreenResult>
    val achievements: Value<ImmutableList<Achievement>>

    data class Achievement (
        val icon: String,
        val iconGray: String,
        val localizedName: String,
        val localizedDescription: String,
        val globalUnlockPercent: String,
        val isHidden: Boolean,
        val isUnlocked: Boolean
    )
}
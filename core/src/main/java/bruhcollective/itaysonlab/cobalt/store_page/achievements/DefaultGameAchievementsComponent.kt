package bruhcollective.itaysonlab.cobalt.store_page.achievements

import bruhcollective.itaysonlab.cobalt.core.commons.CobaltScreenResult
import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.ksteam.EnvironmentConstants
import bruhcollective.itaysonlab.ksteam.models.AppId
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import com.arkivanov.essenty.lifecycle.doOnResume
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

internal class DefaultGameAchievementsComponent (
    private val appId: AppId,
    componentContext: ComponentContext
): GameAchievementsComponent, ComponentContext by componentContext, KoinComponent {
    override val screenResult = MutableValue<CobaltScreenResult>(CobaltScreenResult.Loading)
    override val achievements = MutableValue<ImmutableList<GameAchievementsComponent.Achievement>>(persistentListOf())

    private val scope = coroutineScope()
    private val client by inject<SteamClient>()

    init {
        doOnCreate {
            scope.launch {
                load()
            }
        }
    }

    private suspend fun load() {
        runCatching {
            val globalAchievements = client.ksteam.profile.getGameAchievements(appId)
            val myAchievements = client.ksteam.profile.getTopAchievements(client.ksteam.currentSessionSteamId, appId, count = globalAchievements.size)?.associateBy { it.name } ?: emptyMap()

            val finalAchievements = globalAchievements.mapIndexed { index, achievement ->
                val mineData = myAchievements[achievement.localized_name]

                GameAchievementsComponent.Achievement(
                    icon = EnvironmentConstants.formatCommunityImageUrl(appId.value, achievement.icon.orEmpty()),
                    iconGray = EnvironmentConstants.formatCommunityImageUrl(appId.value, achievement.icon_gray.orEmpty()),
                    localizedName = achievement.localized_name.orEmpty(),
                    localizedDescription = achievement.localized_desc.orEmpty(),
                    globalUnlockPercent = achievement.player_percent_unlocked.orEmpty(),
                    isHidden = achievement.hidden ?: false,
                    isUnlocked = mineData != null,
                )
            }

            achievements.value = finalAchievements.toImmutableList()
            screenResult.value = CobaltScreenResult.Loaded
        }.onFailure { e ->
            screenResult.value = CobaltScreenResult.Error(e)
        }
    }
}
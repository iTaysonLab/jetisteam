package bruhcollective.itaysonlab.cobalt.library.games.alert

import bruhcollective.itaysonlab.cobalt.core.ksteam.SteamClient
import bruhcollective.itaysonlab.cobalt.library.games.alert.GameSheetComponent.Platform
import bruhcollective.itaysonlab.ksteam.models.AppId
import bruhcollective.itaysonlab.ksteam.models.app.SteamApplicationPlaytime
import bruhcollective.itaysonlab.ksteam.models.enums.ELanguage
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import com.arkivanov.essenty.lifecycle.doOnCreate
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
internal class DefaultGameSheetComponent (
    private val id: Int,
    private val onAchievementsClicked: () -> Unit,
    private val onStorePageClicked: () -> Unit,
    private val onRemoteInstallClicked: () -> Unit,
    private val onGameNotesClicked: () -> Unit,
    componentContext: ComponentContext
): GameSheetComponent, ComponentContext by componentContext, KoinComponent {
    private val scope = coroutineScope()
    private val client by inject<SteamClient>()

    override val title = MutableValue("")
    override val sheetBackgroundUrl = MutableValue("")
    override val sheetForegroundUrl = MutableValue("")
    override val achievements = MutableValue<GameSheetComponent.AchievementsState>(GameSheetComponent.AchievementsState.Loading)
    override val playtime = MutableValue<GameSheetComponent.PlaytimeInformation>(GameSheetComponent.PlaytimeInformation())

    init {
        doOnCreate {
            scope.launch(Dispatchers.Default) {
                val appIds = listOf(AppId(id))
                val app = client.ksteam.store.querySteamApplications(appIds).firstOrNull()
                val localizedAssets = app?.assets?.localizedAssets[ELanguage.English]
                val playtimeData = client.ksteam.library.getApplicationPlaytime(appIds.first())

                title.value = app?.name.orEmpty()
                sheetBackgroundUrl.value = (localizedAssets?.libraryHeroBlur?.path ?: localizedAssets?.libraryHero?.path ?: app?.assets?.pageBgRaw)?.takeIf(String::isNotEmpty).orEmpty()
                sheetForegroundUrl.value = (localizedAssets?.libraryLogo?.path)?.takeIf(String::isNotEmpty).orEmpty()

                client.ksteam.profile.getAchievementsProgress(
                    steamId = client.ksteam.currentSessionSteamId,
                    appIds = appIds
                ).values.firstOrNull().let { achievementState ->
                    achievements.value = if (achievementState != null) {
                        GameSheetComponent.AchievementsState.Available(
                            completed = achievementState.unlocked ?: 0,
                            total = achievementState.total ?: 0,
                            percentage = achievementState.percentage ?: 0f
                        )
                    } else {
                        GameSheetComponent.AchievementsState.Unavailable
                    }
                }

                playtimeData?.calculatePlaytimeInformation()?.also { result ->
                    playtime.value = result
                }
            }
        }
    }

    private fun SteamApplicationPlaytime.calculatePlaytimeInformation(): GameSheetComponent.PlaytimeInformation {
        val minutesTotal = playTime.total.inWholeMinutes.toFloat()

        return arrayOf(
            GameSheetComponent.PlatformEntry(
                platform = Platform.Win,
                duration = playTime.windows,
                percentage = playTime.windows.inWholeMinutes / minutesTotal,
                firstLaunch = firstLaunch.windows ?: Instant.DISTANT_PAST,
                lastLaunch = lastLaunch.windows ?: Instant.DISTANT_PAST
            ), GameSheetComponent.PlatformEntry(
                platform = Platform.Linux,
                duration = playTime.linux - playTime.deck,
                percentage = (playTime.linux - playTime.deck).inWholeMinutes / minutesTotal,
                firstLaunch = firstLaunch.linux ?: Instant.DISTANT_PAST,
                lastLaunch = lastLaunch.linux ?: Instant.DISTANT_PAST
            ), GameSheetComponent.PlatformEntry(
                platform = Platform.Mac,
                duration = playTime.mac,
                percentage = playTime.mac.inWholeMinutes / minutesTotal,
                firstLaunch = firstLaunch.mac ?: Instant.DISTANT_PAST,
                lastLaunch = lastLaunch.mac ?: Instant.DISTANT_PAST
            ), GameSheetComponent.PlatformEntry(
                platform = Platform.Deck,
                duration = playTime.deck,
                percentage = playTime.deck.inWholeMinutes / minutesTotal,
                firstLaunch = firstLaunch.deck ?: Instant.DISTANT_PAST,
                lastLaunch = lastLaunch.deck ?: Instant.DISTANT_PAST
            )
        ).filter { it.duration.isPositive() }.toImmutableList().let { platforms ->
            GameSheetComponent.PlaytimeInformation(
                totalPlaytime = playTime.total,
                lastLaunch = lastLaunch.total,
                platformEntries = platforms
            )
        }
    }

    override fun onAchievementsClicked() {
        onAchievementsClicked.invoke()
    }

    override fun onRemoteInstallClicked() {
        onRemoteInstallClicked.invoke()
    }

    override fun onStorePageClicked() {
        onStorePageClicked.invoke()
    }

    override fun onGameNotesClicked() {
        onGameNotesClicked.invoke()
    }
}
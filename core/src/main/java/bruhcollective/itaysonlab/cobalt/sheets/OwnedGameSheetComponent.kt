package bruhcollective.itaysonlab.cobalt.sheets

import com.arkivanov.decompose.value.Value
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlin.time.Duration
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

/**
 * A game sheet for owned games.
 *
 * Has achievements, remote install, playtime information.
 */
interface OwnedGameSheetComponent {
    val title: Value<String>

    val sheetBackgroundUrl: Value<String>
    val sheetForegroundUrl: Value<String>
    val achievements: Value<AchievementsState>
    val playtime: Value<PlaytimeInformation>

    fun onAchievementsClicked()
    fun onRemoteInstallClicked()
    fun onStorePageClicked()
    fun onGameNotesClicked()

    @OptIn(ExperimentalTime::class)
    data class PlaytimeInformation (
        val totalPlaytime: Duration = Duration.Companion.ZERO,
        val lastLaunch: Instant? = null,
        val platformEntries: ImmutableList<PlatformEntry> = persistentListOf()
    )

    sealed interface AchievementsState {
        data class Available (
            val completed: Int,
            val total: Int,
            val percentage: Float
        ): AchievementsState

        data object Unavailable:
            AchievementsState
        data object Loading:
            AchievementsState
    }

    enum class Platform {
        Win, Linux, Mac, Deck
    }

    @OptIn(ExperimentalTime::class)
    data class PlatformEntry (
        val platform: Platform,
        val duration: Duration,
        val percentage: Float,
        val firstLaunch: Instant,
        val lastLaunch: Instant,
    )
}
package bruhcollective.itaysonlab.microapp.library.ui.bottomsheet

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.controllers.CdnController
import bruhcollective.itaysonlab.jetisteam.ext.roundUpTo
import bruhcollective.itaysonlab.jetisteam.usecases.game.GetGameAchievementCard
import bruhcollective.itaysonlab.microapp.core.ext.getProto
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.library.LibraryMicroapp
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import steam.player.CPlayer_GetOwnedGames_Response_Game
import javax.inject.Inject

@HiltViewModel
class OwnedGameBottomSheetViewModel @Inject constructor(
    savedState: SavedStateHandle,
    private val getGameAchievementCard: GetGameAchievementCard,
    private val cdnController: CdnController
) : ViewModel() {
    private val steamId = savedState.getSteamId()
    private val gameInfo = savedState.getProto<CPlayer_GetOwnedGames_Response_Game>(LibraryMicroapp.Arguments.GameData.name)

    val longSteamId = steamId.steamId
    val appId = gameInfo.appid ?: 0
    val gameName = gameInfo.name.orEmpty()

    var headerBgUrl by mutableStateOf<BlurrableImageUrl?>(null)
        private set

    var headerLogoUrl by mutableStateOf<String?>(null)
        private set

    val totalPlaytime = "${((gameInfo.playtime_forever ?: 0) / 60f).roundUpTo(1)} h"

    var achievementCardState by mutableStateOf<AchievementCardState>(AchievementCardState.Loading)
        private set

    init {
        viewModelScope.launch {
            loadPictureUrls()
            loadAchievements()
        }
    }

    private suspend fun loadPictureUrls() {
        val libraryHeroUrl = cdnController.buildAppUrl(appId, "library_hero.jpg")
        val logoUrl = cdnController.buildAppUrl(appId, "logo.png")
        val fallbackUrl = cdnController.buildAppUrl(appId, "portrait.png")

        headerBgUrl = if (cdnController.exists(libraryHeroUrl)) {
            BlurrableImageUrl(libraryHeroUrl to false)
        } else {
            BlurrableImageUrl(fallbackUrl to true)
        }

        headerLogoUrl = if (cdnController.exists(logoUrl)) {
            logoUrl
        } else {
            ""
        }
    }

    private suspend fun loadAchievements() {
        achievementCardState = try {
            getGameAchievementCard(steamId, appId).let { prg ->
                AchievementCardState.Ready(
                    available = (prg.total ?: 0) > 0,
                    formattedString = "${prg.unlocked} / ${prg.total}",
                    percentage = (prg.percentage ?: 0f) / 100f
                )
            }
        } catch (e: Exception) {
            AchievementCardState.Error
        }
    }

    sealed class AchievementCardState {
        class Ready(val available: Boolean, val formattedString: String, val percentage: Float) :
            AchievementCardState()

        object Loading : AchievementCardState()
        object Error : AchievementCardState()
    }

    @JvmInline
    value class BlurrableImageUrl(private val packed: Pair<String, Boolean>) {
        val url get() = packed.first
        val blur get() = packed.second
    }
}
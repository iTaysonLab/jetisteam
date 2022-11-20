package bruhcollective.itaysonlab.microapp.profile.ui

import androidx.lifecycle.SavedStateHandle
import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.profile.GetProfileData
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import dagger.hilt.android.lifecycle.HiltViewModel
import steam.player.CPlayer_GetAchievementsProgress_Response_AchievementProgress
import steam.player.CPlayer_GetOwnedGames_Response_Game
import javax.inject.Inject

@HiltViewModel
internal class ProfileScreenViewModel @Inject constructor(
    private val getProfileData: GetProfileData,
    savedState: SavedStateHandle,
    steamSessionController: SteamSessionController
): PageViewModel<GetProfileData.ProfileData>() {
    val savedStateId = savedState.get<Long>(CommonArguments.SteamId.name)
    val isRootScreen = savedStateId == 0L

    val steamId = if (isRootScreen) {
        steamSessionController.steamId()
    } else {
        savedState.getSteamId()
    }

    init { reload() }

    override suspend fun load() = getProfileData(steamId)

    fun gameToAchievements(id: Int) = data!!.let { data ->
        val unknownApp = data.otherAppsInfo[id]

        data.ownedGames.getOrDefault(id, CPlayer_GetOwnedGames_Response_Game(
            name = unknownApp?.second.orEmpty()
        )) to data.achievementsProgress.getOrDefault(id, CPlayer_GetAchievementsProgress_Response_AchievementProgress(
            total = 0,
            unlocked = 0,
            percentage = 0f
        ))
    }

    fun game(id: Int) = data!!.ownedGames[id]!!

    fun gameSize() = data?.ownedGames?.size ?: 0
}

typealias Game = CPlayer_GetOwnedGames_Response_Game
typealias GameToAchievements = Pair<CPlayer_GetOwnedGames_Response_Game, CPlayer_GetAchievementsProgress_Response_AchievementProgress>
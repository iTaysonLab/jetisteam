package bruhcollective.itaysonlab.microapp.profile.ui

import androidx.lifecycle.SavedStateHandle
import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.GetProfileData
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import dagger.hilt.android.lifecycle.HiltViewModel
import steam.player.CPlayer_GetAchievementsProgress_Response_AchievementProgress
import steam.player.CPlayer_GetOwnedGames_Response_Game
import javax.inject.Inject

@HiltViewModel
internal class ProfileScreenViewModel @Inject constructor(
    private val getProfileData: GetProfileData,
    private val savedState: SavedStateHandle,
    steamSessionController: SteamSessionController
): PageViewModel<GetProfileData.ProfileData>() {
    val steamId = if (savedState.get<Long>(CommonArguments.SteamId.name) == 0L) {
        steamSessionController.steamId()
    } else {
        savedState.getSteamId()
    }

    init { reload() }

    override suspend fun load() = getProfileData(steamId)

    fun gameToAchievements(id: Int) = data!!.let { data ->
        data.ownedGames[id]!! to data.achievementsProgress[id]!!
    }

    fun game(id: Int) = data!!.ownedGames[id]!!

    fun gameSize() = data?.ownedGames?.size ?: 0
}

typealias Game = CPlayer_GetOwnedGames_Response_Game
typealias GameToAchievements = Pair<CPlayer_GetOwnedGames_Response_Game, CPlayer_GetAchievementsProgress_Response_AchievementProgress>
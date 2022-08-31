package bruhcollective.itaysonlab.microapp.profile.ui

import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.GetProfileData
import dagger.hilt.android.lifecycle.HiltViewModel
import steam.player.CPlayer_GetAchievementsProgress_Response_AchievementProgress
import steam.player.CPlayer_GetOwnedGames_Response_Game
import javax.inject.Inject

@HiltViewModel
internal class ProfileScreenViewModel @Inject constructor(
    private val getProfileData: GetProfileData
): PageViewModel<GetProfileData.ProfileData>() {
    init { reload() }
    override suspend fun load() = getProfileData()

    fun gameToAchievements(id: Int) = data!!.let { data ->
        data.ownedGames[id]!! to data.achievementsProgress[id]!!
    }

    fun game(id: Int) = data!!.ownedGames[id]!!
    fun gameSize() = data?.ownedGames?.size ?: 0
}

typealias Game = CPlayer_GetOwnedGames_Response_Game
typealias GameToAchievements = Pair<CPlayer_GetOwnedGames_Response_Game, CPlayer_GetAchievementsProgress_Response_AchievementProgress>
package bruhcollective.itaysonlab.microapp.profile.ui

import androidx.lifecycle.SavedStateHandle
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.GetProfileData
import bruhcollective.itaysonlab.microapp.profile.ProfileMicroappImpl.InternalRoutes.ARG_ID
import bruhcollective.itaysonlab.microapp.profile.ProfileMicroappImpl.InternalRoutes.ARG_MY_PROFILE
import dagger.hilt.android.lifecycle.HiltViewModel
import steam.player.CPlayer_GetAchievementsProgress_Response_AchievementProgress
import steam.player.CPlayer_GetOwnedGames_Response_Game
import javax.inject.Inject

@HiltViewModel
internal class ProfileScreenViewModel @Inject constructor(
    private val getProfileData: GetProfileData,
    private val savedState: SavedStateHandle
): PageViewModel<GetProfileData.ProfileData>() {
    init { reload() }
    override suspend fun load() = savedState.get<String>(ARG_ID)!!.let {
        if (it == ARG_MY_PROFILE) getProfileData()
        else getProfileData(SteamID(it.toLong()))
    }

    fun gameToAchievements(id: Int) = data!!.let { data ->
        data.ownedGames[id]!! to data.achievementsProgress[id]!!
    }

    fun game(id: Int) = data!!.ownedGames[id]!!
    fun gameSize() = data?.ownedGames?.size ?: 0
}

typealias Game = CPlayer_GetOwnedGames_Response_Game
typealias GameToAchievements = Pair<CPlayer_GetOwnedGames_Response_Game, CPlayer_GetAchievementsProgress_Response_AchievementProgress>
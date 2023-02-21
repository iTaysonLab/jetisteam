package bruhcollective.itaysonlab.microapp.profile.ui

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.ksteam.handlers.persona
import bruhcollective.itaysonlab.ksteam.handlers.profile
import bruhcollective.itaysonlab.ksteam.handlers.store
import bruhcollective.itaysonlab.ksteam.models.AppId
import bruhcollective.itaysonlab.ksteam.models.library.OwnedGame
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileCustomization
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileEquipment
import bruhcollective.itaysonlab.ksteam.models.persona.SummaryPersona
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.withContext
import steam.webui.player.CPlayer_GetAchievementsProgress_Response_AchievementProgress
import javax.inject.Inject

@HiltViewModel
internal class ProfileScreenViewModel @Inject constructor(
    private val steamClient: HostSteamClient,
    savedState: SavedStateHandle,
): PageViewModel<ProfileScreenViewModel.ReadyState>() {
    val savedStateId = savedState.get<Long>(CommonArguments.SteamId.name)
    val isRootScreen = savedStateId == 0L

    val steamId = if (isRootScreen) {
        steamClient.currentSteamId
    } else {
        savedState.getSteamId()
    }

    init {
        reload()
    }

    override suspend fun load(): ReadyState = withContext(Dispatchers.IO) {
        ReadyState(
            personaSummaryState = steamClient.client.profile.getProfile(steamId).stateIn(viewModelScope),
            personaState = steamClient.client.persona.persona(steamId).stateIn(viewModelScope),
            customization = steamClient.client.profile.getCustomization(steamId),
            equipment = steamClient.client.profile.getEquipment(steamId)
        )
    }

    suspend fun getAppSummary(appId: AppId) = steamClient.client.store.getAppSummaries(listOf(appId))[appId]
    suspend fun getLocalizationForRichPresence(appId: AppId) = steamClient.client.store.getRichPresenceLocalization(appId)

    data class ReadyState(
        val personaSummaryState: StateFlow<SummaryPersona>,
        // We use this to provide the most accurate data for friends,
        val personaState: StateFlow<Persona>,
        val customization: ProfileCustomization,
        val equipment: ProfileEquipment
    )
}

typealias Game = OwnedGame
typealias GameToAchievements = Pair<OwnedGame, CPlayer_GetAchievementsProgress_Response_AchievementProgress>
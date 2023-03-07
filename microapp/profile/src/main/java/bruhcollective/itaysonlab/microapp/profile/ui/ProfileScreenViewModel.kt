package bruhcollective.itaysonlab.microapp.profile.ui

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.ksteam.handlers.persona
import bruhcollective.itaysonlab.ksteam.handlers.profile
import bruhcollective.itaysonlab.ksteam.handlers.store
import bruhcollective.itaysonlab.ksteam.models.apps.AppSummary
import bruhcollective.itaysonlab.ksteam.models.enums.EFriendRelationship
import bruhcollective.itaysonlab.ksteam.models.library.OwnedGame
import bruhcollective.itaysonlab.ksteam.models.persona.Persona
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileCustomization
import bruhcollective.itaysonlab.ksteam.models.persona.ProfileEquipment
import bruhcollective.itaysonlab.ksteam.models.persona.SummaryPersona
import bruhcollective.itaysonlab.ksteam.util.RichPresenceFormatter
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
    private val savedStateId = savedState.get<Long>(CommonArguments.SteamId.name)
    val isRootScreen = savedStateId == 0L

    val steamId = if (isRootScreen) {
        steamClient.currentSteamId
    } else {
        savedState.getSteamId()
    }

    var richPresenceInformation by mutableStateOf<RichPresence?>(null)
        private set

    init {
        reload()
    }

    override suspend fun load(): ReadyState = withContext(Dispatchers.IO) {
        ReadyState(
            personaSummaryState = steamClient.client.profile.getProfile(steamId).stateIn(viewModelScope),
            personaState = steamClient.client.persona.persona(steamId).stateIn(viewModelScope),
            customization = steamClient.client.profile.getCustomization(steamId),
            equipment = steamClient.client.profile.getEquipment(steamId),
            relationship = steamClient.client.persona.personaRelationship(steamId).stateIn(viewModelScope),
        )
    }

    suspend fun updateRichPresenceData(persona: Persona) {
        richPresenceInformation = if (persona.ingameAppId.id > 0) {
            val formatted = RichPresenceFormatter.formatRp(steamRp = persona.ingameRichPresence, localizationTokens = steamClient.client.store.getRichPresenceLocalization(persona.ingameAppId))
            val summary = steamClient.client.store.getAppSummaries(listOf(persona.ingameAppId)).values.firstOrNull() ?: return

            RichPresence(
                formattedString = formatted.formattedString,
                numbersInGame = formatted.groupSize,
                appSummary = summary
            )
        } else {
            null
        }
    }

    data class ReadyState(
        val personaSummaryState: StateFlow<SummaryPersona>,
        // We use this to provide the most accurate data for friends,
        val personaState: StateFlow<Persona>,
        val customization: ProfileCustomization,
        val equipment: ProfileEquipment,
        val relationship: StateFlow<EFriendRelationship>
    )

    @Immutable
    data class RichPresence(
        val formattedString: String,
        val numbersInGame: Int,
        val appSummary: AppSummary
    )
}

typealias Game = OwnedGame
typealias GameToAchievements = Pair<OwnedGame, CPlayer_GetAchievementsProgress_Response_AchievementProgress>
package bruhcollective.itaysonlab.microapp.profile.ui.screens.editsections

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import bruhcollective.itaysonlab.jetisteam.controllers.CdnController
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.profile.GetProfileEquipsAndOwned
import bruhcollective.itaysonlab.jetisteam.usecases.profile.SetProfileEquipment
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.profile.ProfileMicroapp
import bruhcollective.itaysonlab.microapp.profile.core.SectionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import steam.player.ECommunityItemClass
import javax.inject.Inject

@HiltViewModel
internal class ProfileEditSectionViewModel @Inject constructor(
    private val getProfileEquips: GetProfileEquipsAndOwned,
    private val setProfileEquipment: SetProfileEquipment,
    private val cdnController: CdnController,
    savedStateHandle: SavedStateHandle
): PageViewModel<GetProfileEquipsAndOwned.ProfileEquips>() {
    private var initialItemId = 0L

    var availableItems by mutableStateOf<List<Item>>(emptyList())
        private set

    var currentItemId by mutableStateOf(0L)
        private set

    var hasAnyChanges by mutableStateOf(false)
        private set

    var isCommittingChanges by mutableStateOf(false)
        private set

    val steamId = savedStateHandle.getSteamId()
    val currentSectionType = savedStateHandle.get<SectionType>(ProfileMicroapp.Arguments.SectionType.name) ?: SectionType.General

    val apiFilter = when (currentSectionType) {
        SectionType.Avatar -> ECommunityItemClass.k_ECommunityItemClass_AnimatedAvatar
        SectionType.AvatarFrame -> ECommunityItemClass.k_ECommunityItemClass_AvatarFrame
        SectionType.ProfileBackground -> ECommunityItemClass.k_ECommunityItemClass_ProfileBackground
        SectionType.MiniprofileBackground -> ECommunityItemClass.k_ECommunityItemClass_MiniProfileBackground
        else -> error("General/Theme section should be separated from generic list screens")
    }

    init { reload() }

    override suspend fun load() = getProfileEquips(steamId, apiFilter).also { equipment ->
        currentItemId = equipment.current?.itemId ?: 0L
        initialItemId = currentItemId

        availableItems = equipment.owned.map { item ->
            val app = equipment.apps[item.appId] ?: error("Unknown app for profile owned item [id = ${item.appId}]")

            Item(
                id = item.itemId,
                name = item.name,
                fromApplication = app.name.orEmpty(),
                fromApplicationIcon = cdnController.buildCommunityUrl("images/apps/${app.appid}/${app.assets?.community_icon}.jpg"),
                itemPreview = item.imageLarge.orEmpty()
            )
        }
    }

    fun switchItemSelection(itemId: Long) {
        currentItemId = itemId
        hasAnyChanges = itemId != initialItemId
    }

    fun commitChanges(onChangesCommitted: () -> Unit) {
        if (isCommittingChanges || !hasAnyChanges) return

        viewModelScope.launch {
            isCommittingChanges = true

            setProfileEquipment(currentItemId, apiFilter)
            onChangesCommitted()

            isCommittingChanges = false
        }
    }

    class Item(
        val id: Long,
        val name: String,
        val fromApplication: String,
        val fromApplicationIcon: String,
        val itemPreview: String
    )
}
package bruhcollective.itaysonlab.microapp.profile.ui.screens.edit

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import bruhcollective.itaysonlab.jetisteam.uikit.vm.PageViewModel
import bruhcollective.itaysonlab.jetisteam.usecases.profile.GetProfileEquips
import bruhcollective.itaysonlab.microapp.core.ext.getSteamId
import bruhcollective.itaysonlab.microapp.profile.R
import bruhcollective.itaysonlab.microapp.profile.core.SectionType
import com.squareup.wire.get
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class ProfileEditViewModel @Inject constructor(
    private val getProfileEquips: GetProfileEquips,
    savedStateHandle: SavedStateHandle
): PageViewModel<GetProfileEquips.ProfileEquips>() {
    var uiSections by mutableStateOf<List<Block>>(emptyList())
        private set

    val steamId = savedStateHandle.getSteamId()

    init { reload() }

    override suspend fun load() = getProfileEquips().also { equipment ->
        uiSections = buildList {
            add(Block(
                title = R.string.edit_type_avatar,
                text = equipment.equipment.animatedAvatar?.name,
                icon = { Icons.Rounded.Photo },
                type = SectionType.Avatar
            ))

            add(Block(
                title = R.string.edit_type_avatar_frame,
                text = equipment.equipment.avatarFrame?.name,
                icon = { Icons.Rounded.CropFree },
                type = SectionType.AvatarFrame
            ))

            add(Block(
                title = R.string.edit_type_profile_bg,
                text = equipment.equipment.background?.name,
                icon = { Icons.Rounded.Wallpaper },
                type = SectionType.ProfileBackground
            ))

            add(Block(
                title = R.string.edit_type_miniprofile_bg,
                text = equipment.equipment.miniBackground?.name,
                icon = { Icons.Rounded.PictureInPicture },
                type = SectionType.MiniprofileBackground
            ))

            add(Block(
                title = R.string.edit_type_theme,
                text = equipment.equipment.profileModifier?.name ?: equipment.currentTheme?.title,
                icon = { Icons.Rounded.Palette },
                type = SectionType.ProfileTheme
            ))
        }
    }

    class Block(
        @StringRes val title: Int,
        val text: String?,
        val icon: () -> ImageVector,
        val type: SectionType
    )
}
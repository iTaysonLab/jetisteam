package bruhcollective.itaysonlab.microapp.profile.core

/*import bruhcollective.itaysonlab.jetisteam.mappers.ProfileEquipment
import bruhcollective.itaysonlab.jetisteam.mappers.ProfileItem
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.results.NavigationResult
import kotlinx.parcelize.Parcelize

internal sealed class ProfileEditEvent: NavigationResult {
    @Parcelize
    internal class ProfileItemChanged(
        val type: SectionType,
        val item: ProfileItem
    ): ProfileEditEvent()
}

internal fun ProfileEquipment.enrichWithEvent(event: ProfileEditEvent.ProfileItemChanged): ProfileEquipment {
    return copy(
        background = if (event.type == SectionType.ProfileBackground) event.item else background,
        miniBackground = if (event.type == SectionType.MiniprofileBackground) event.item else miniBackground,
        avatarFrame = if (event.type == SectionType.AvatarFrame) event.item else avatarFrame,
        animatedAvatar = if (event.type == SectionType.Avatar) event.item else animatedAvatar,
    )
}*/
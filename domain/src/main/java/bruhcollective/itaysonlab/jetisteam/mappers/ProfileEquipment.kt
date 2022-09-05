package bruhcollective.itaysonlab.jetisteam.mappers

import steam.player.CPlayer_GetProfileItemsEquipped_Response

class ProfileEquipment(
    val background: ProfileItem?,
    val miniBackground: ProfileItem?,
    val avatarFrame: ProfileItem?,
    val animatedAvatar: ProfileItem?,
    val profileModifier: ProfileItem?,
) {
    constructor(proto: CPlayer_GetProfileItemsEquipped_Response): this(
        background = proto.profile_background.toAppModel(),
        miniBackground = proto.mini_profile_background.toAppModel(),
        avatarFrame = proto.avatar_frame.toAppModel(),
        animatedAvatar = proto.animated_avatar.toAppModel(),
        profileModifier = proto.profile_modifier.toAppModel(),
    )
}
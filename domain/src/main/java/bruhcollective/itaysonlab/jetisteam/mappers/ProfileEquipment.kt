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
        background = proto.profileBackground.toAppModel(),
        miniBackground = proto.miniProfileBackground.toAppModel(),
        avatarFrame = proto.avatarFrame.toAppModel(),
        animatedAvatar = proto.animatedAvatar.toAppModel(),
        profileModifier = proto.profileModifier.toAppModel(),
    )
}
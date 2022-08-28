package bruhcollective.itaysonlab.jetisteam.mappers

import steam.player.EProfileCustomizationType
import steam.player.CPlayer_GetProfileCustomization_Response
import steam.player.EBanContentCheckResult
import steam.player.EProfileCustomizationStyle

class ProfileCustomization(
    val profileCustomizationEntries: List<ProfileCustomizationEntry>,
    val slotsAvailable: Int,
    val profileTheme: ProfileTheme,
    val profilePreferences: ProfilePreferences
) {
    constructor(proto: CPlayer_GetProfileCustomization_Response): this(
        profileCustomizationEntries = proto.customizationsList.map { ProfileCustomizationEntry(it) },
        slotsAvailable = proto.slotsAvailable,
        profileTheme = ProfileTheme(proto.profileTheme),
        profilePreferences = ProfilePreferences(proto.profilePreferences)
    )
}

class ProfileCustomizationEntry(
    val customizationType: EProfileCustomizationType,
    val level: Int,
    val active: Boolean,
    val large: Boolean,
    val style: EProfileCustomizationStyle,
    val slots: List<ProfileCustomizationSlot>
) {
    constructor(proto: steam.player.ProfileCustomization): this(
        customizationType = proto.customizationType,
        level = proto.level,
        active = proto.active,
        large = proto.large,
        style = proto.customizationStyle,
        slots = proto.slotsList.map { ProfileCustomizationSlot(it) },
    )
}

class ProfileCustomizationSlot(
    val appId: Int,
    val publishedFileId: Long,
    val itemAssetId: Long,
    val itemContextId: Long,
    val notes: String,
    val title: String,
    val accountId: Int,
    val badgeId: Int,
    val borderColor: Int,
    val itemClassId: Long,
    val itemInstanceId: Long,
    val banResult: EBanContentCheckResult
) {
    constructor(proto: steam.player.ProfileCustomizationSlot): this(
        appId = proto.appid,
        publishedFileId = proto.publishedfileid,
        itemAssetId = proto.itemAssetid,
        itemContextId = proto.itemContextid,
        notes = proto.notes,
        title = proto.title,
        accountId = proto.accountid,
        badgeId = proto.badgeid,
        borderColor = proto.borderColor,
        itemClassId = proto.itemClassid,
        itemInstanceId = proto.itemInstanceid,
        banResult = proto.banCheckResult,
    )
}

class ProfileTheme(
    val themeId: String,
    val title: String
) {
    constructor(proto: steam.player.ProfileTheme): this(
        themeId = proto.themeId,
        title = proto.title
    )
}

class ProfilePreferences(
    val hideProfileAwards: Boolean
) {
    constructor(proto: steam.player.ProfilePreferences): this(
        hideProfileAwards = proto.hideProfileAwards
    )
}
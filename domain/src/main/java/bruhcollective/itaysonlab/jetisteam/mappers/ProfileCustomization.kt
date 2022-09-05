package bruhcollective.itaysonlab.jetisteam.mappers

import steam.player.EProfileCustomizationType
import steam.player.CPlayer_GetProfileCustomization_Response
import steam.player.EBanContentCheckResult
import steam.player.EProfileCustomizationStyle

class ProfileCustomization(
    val profileCustomizationEntries: List<ProfileCustomizationEntry>,
    val slotsAvailable: Int,
    val profileTheme: ProfileTheme,
    val profilePreferences: ProfilePreferences?
) {
    constructor(proto: CPlayer_GetProfileCustomization_Response): this(
        profileCustomizationEntries = proto.customizations.map { ProfileCustomizationEntry(it) },
        slotsAvailable = proto.slots_available ?: 0,
        profileTheme = proto.profile_theme?.let { ProfileTheme(it) } ?: error("Unknown ProfileTheme"),
        profilePreferences = proto.profile_preferences?.let { ProfilePreferences(it) }
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
        customizationType = proto.customization_type ?: error("Unknown CustomizationType"),
        level = proto.level ?: 0,
        active = proto.active ?: false,
        large = proto.large ?: false,
        style = proto.customization_style ?: EProfileCustomizationStyle.k_EProfileCustomizationStyleDefault,
        slots = proto.slots.map { ProfileCustomizationSlot(it) },
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
        appId = proto.appid ?: 0,
        publishedFileId = proto.publishedfileid ?: 0,
        itemAssetId = proto.item_assetid ?: 0,
        itemContextId = proto.item_contextid ?: 0,
        notes = proto.notes.orEmpty(),
        title = proto.title.orEmpty(),
        accountId = proto.accountid ?: 0,
        badgeId = proto.badgeid ?: 0,
        borderColor = proto.border_color ?: 0,
        itemClassId = proto.item_classid ?: 0,
        itemInstanceId = proto.item_instanceid ?: 0,
        banResult = proto.ban_check_result ?: EBanContentCheckResult.k_EBanContentCheckResult_NotScanned,
    )
}

class ProfileTheme(
    val themeId: String,
    val title: String
) {
    constructor(proto: steam.player.ProfileTheme): this(
        themeId = proto.theme_id.orEmpty(),
        title = proto.title.orEmpty()
    )
}

class ProfilePreferences(
    val hideProfileAwards: Boolean
) {
    constructor(proto: steam.player.ProfilePreferences): this(
        hideProfileAwards = proto.hide_profile_awards ?: false
    )
}
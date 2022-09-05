package bruhcollective.itaysonlab.jetisteam.mappers

import bruhcollective.itaysonlab.jetisteam.util.CdnUrlUtil
import steam.player.CPlayer_GetOwnedGames_Response_Game

class OwnedGame(
    val appid: Int,
    val name: String,
    val playtime: OwnedGamePlaytime,
    val icon: String,
    val logo: String,
    val capsule: String,
    val communityVisibleStats: Boolean,
    val hasWorkshop: Boolean,
    val hasMarket: Boolean,
    val hasDlc: Boolean,
) {
    constructor(proto: CPlayer_GetOwnedGames_Response_Game): this(
        appid = proto.appid ?: 0,
        name = proto.name.orEmpty(),
        icon = proto.img_icon_url.orEmpty(),
        logo = proto.img_logo_url.orEmpty(),
        capsule = CdnUrlUtil.buildAppUrl(proto.appid ?: 0, if (proto.capsule_filename != null) {
            proto.capsule_filename!!
        } else {
            "library_600x900.jpg"
        }) ,
        communityVisibleStats = proto.has_community_visible_stats ?: false,
        hasWorkshop = proto.has_workshop ?: false,
        hasMarket = proto.has_market ?: false,
        hasDlc = proto.has_dlc ?: false,
        playtime = OwnedGamePlaytime(proto)
    )
}

class OwnedGamePlaytime(
    val twoWeeks: Int,
    val forever: Int,
    val onWindows: Int,
    val onMac: Int,
    val onLinux: Int,
    val lastPlayed: Int
) {
    constructor(proto: CPlayer_GetOwnedGames_Response_Game): this(
        twoWeeks = proto.playtime_2weeks ?: 0,
        forever = proto.playtime_forever ?: 0,
        onWindows = proto.playtime_windows_forever ?: 0,
        onMac = proto.playtime_mac_forever ?: 0,
        onLinux = proto.playtime_linux_forever ?: 0,
        lastPlayed = proto.rtime_last_played ?: 0
    )
}
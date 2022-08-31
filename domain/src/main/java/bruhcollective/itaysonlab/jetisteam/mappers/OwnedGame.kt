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
        appid = proto.appid,
        name = proto.name,
        icon = proto.imgIconUrl,
        logo = proto.imgLogoUrl,
        capsule = CdnUrlUtil.buildAppUrl(proto.appid, if (proto.hasCapsuleFilename()) {
            proto.capsuleFilename
        } else {
            "library_600x900.jpg"
        }) ,
        communityVisibleStats = proto.hasCommunityVisibleStats,
        hasWorkshop = proto.hasWorkshop,
        hasMarket = proto.hasMarket,
        hasDlc = proto.hasDlc,
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
        twoWeeks = proto.playtime2Weeks,
        forever = proto.playtimeForever,
        onWindows = proto.playtimeWindowsForever,
        onMac = proto.playtimeMacForever,
        onLinux = proto.playtimeLinuxForever,
        lastPlayed = proto.rtimeLastPlayed
    )
}
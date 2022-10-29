package bruhcollective.itaysonlab.jetisteam.mappers

import bruhcollective.itaysonlab.jetisteam.util.CdnUrlUtil
import steam.player.CPlayer_GetOwnedGames_Response_Game

class OwnedGame(
    val appid: Int,
    val iconUrl: String,
    val capsuleUrl: String,
    val proto: CPlayer_GetOwnedGames_Response_Game
) {
    constructor(proto: CPlayer_GetOwnedGames_Response_Game): this(
        appid = proto.appid ?: 0,
        iconUrl = proto.img_icon_url.orEmpty(),
        capsuleUrl = CdnUrlUtil.buildAppUrl(proto.appid ?: 0, if (proto.capsule_filename != null) {
            proto.capsule_filename!!
        } else {
            "library_600x900.jpg"
        }) ,
        proto = proto
    )
}
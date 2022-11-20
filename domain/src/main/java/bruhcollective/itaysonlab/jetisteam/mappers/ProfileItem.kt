package bruhcollective.itaysonlab.jetisteam.mappers

import bruhcollective.itaysonlab.jetisteam.util.CdnUrlUtil

class ProfileItem(
    val imageSmall: String?,
    val imageLarge: String?,
    val name: String,
    val itemTitle: String?,
    val itemDescription: String?,
    val appId: Int,
    val itemType: Int,
    val itemClass: Int,
    val movieWebm: String?,
    val movieMp4: String?,
    val movieWebmSmall: String?,
    val movieMp4Small: String?,
    val flags: Int,
    val itemId: Long
) {
    constructor(proto: steam.player.ProfileItem): this(
        imageSmall = CdnUrlUtil.buildPublicUrl(proto.image_small),
        imageLarge = CdnUrlUtil.buildPublicUrl(proto.image_large),
        name = proto.name.orEmpty(),
        itemTitle = proto.item_title.orEmpty(),
        itemDescription = proto.item_description.orEmpty(),
        appId = proto.appid ?: 0,
        itemType = proto.item_type ?: 0,
        itemClass = proto.item_class ?: 0,
        movieWebm = CdnUrlUtil.buildPublicUrl(proto.movie_webm),
        movieMp4 = CdnUrlUtil.buildPublicUrl(proto.movie_mp4),
        movieWebmSmall = CdnUrlUtil.buildPublicUrl(proto.movie_webm_small),
        movieMp4Small = CdnUrlUtil.buildPublicUrl(proto.movie_mp4_small),
        flags = proto.equipped_flags ?: 0,
        itemId = proto.communityitemid ?: 0
    )
}

fun steam.player.ProfileItem?.toAppModel() = this?.let { ProfileItem(it) }
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
    val flags: Int
) {
    constructor(proto: steam.player.ProfileItem): this(
        imageSmall = CdnUrlUtil.buildPublicUrl(proto.imageSmall),
        imageLarge = CdnUrlUtil.buildPublicUrl(proto.imageLarge),
        name = proto.name,
        itemTitle = proto.itemTitle,
        itemDescription = proto.itemDescription,
        appId = proto.appid,
        itemType = proto.itemType,
        itemClass = proto.itemClass,
        movieWebm = CdnUrlUtil.buildPublicUrl(proto.movieWebm),
        movieMp4 = CdnUrlUtil.buildPublicUrl(proto.movieMp4),
        movieWebmSmall = CdnUrlUtil.buildPublicUrl(proto.movieWebmSmall),
        movieMp4Small = CdnUrlUtil.buildPublicUrl(proto.movieMp4Small),
        flags = proto.equippedFlags
    )
}

fun steam.player.ProfileItem?.toAppModel() = this?.let { ProfileItem(it) }
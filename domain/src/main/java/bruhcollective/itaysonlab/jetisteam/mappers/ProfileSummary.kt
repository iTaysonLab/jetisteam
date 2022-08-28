package bruhcollective.itaysonlab.jetisteam.mappers

import steam.mobileapp.CMobileApp_GetMobileSummary_Response

class ProfileSummary(
    val ownedGames: Int,
    val friendCount: Int,
    val walletBalance: String
) {
    constructor(proto: CMobileApp_GetMobileSummary_Response): this(
        ownedGames = proto.ownedGames,
        friendCount = proto.friendCount,
        walletBalance = proto.walletBalance
    )
}
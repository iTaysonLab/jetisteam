package bruhcollective.itaysonlab.jetisteam.mappers

import steam.mobileapp.CMobileApp_GetMobileSummary_Response

class ProfileSummary(
    val ownedGames: Int,
    val friendCount: Int,
    val walletBalance: String
) {
    constructor(proto: CMobileApp_GetMobileSummary_Response): this(
        ownedGames = proto.owned_games ?: 0,
        friendCount = proto.friend_count ?: 0,
        walletBalance = proto.wallet_balance.orEmpty()
    )
}
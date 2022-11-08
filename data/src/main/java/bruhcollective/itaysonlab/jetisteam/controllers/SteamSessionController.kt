package bruhcollective.itaysonlab.jetisteam.controllers

import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.proto.SessionData
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SteamSessionController @Inject constructor(
    cfgService: ConfigService,
) {
    internal var authSession by cfgService.protoCfg<SessionData>(key = "steam.session")
        private set

    internal fun buildSteamLoginSecureCookie(): String {
        return "${authSession!!.steam_id}||${authSession!!.access_token}"
    }

    fun writeSession(msg: SessionData) {
        authSession = msg
    }

    fun signedIn() = authSession != null
    fun steamId() = SteamID(authSession?.steam_id ?: 0L)
    fun isMe(steamid: SteamID) = steamId().equalsWith(steamid)
}
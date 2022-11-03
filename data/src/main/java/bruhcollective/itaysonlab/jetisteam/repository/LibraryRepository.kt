package bruhcollective.itaysonlab.jetisteam.repository

import bruhcollective.itaysonlab.jetisteam.controllers.CacheService
import bruhcollective.itaysonlab.jetisteam.controllers.LocaleService
import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import steam.player.CPlayer_GetOwnedGames_Request
import steam.player.CPlayer_GetOwnedGames_Response
import steam.player.CPlayer_GetOwnedGames_Response_Game
import steam.player.Player
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.hours

@Singleton
class LibraryRepository @Inject constructor(
    steamRpcClient: SteamRpcClient,
    private val cacheService: CacheService,
    private val localeService: LocaleService,
    private val sessionController: SteamSessionController
) {
    companion object {
        private val LibraryCacheDuration = 24.hours
    }

    private val playerStub = steamRpcClient.create<Player>()

    suspend fun getLibrary(steamId: SteamID, force: Boolean = false): List<CPlayer_GetOwnedGames_Response_Game> {
        if (!sessionController.isMe(steamId)) {
            return getLibraryNetwork(steamId).games
        }

        return cacheService.protoEntry(
            key = key(steamId),
            adapter = CPlayer_GetOwnedGames_Response.ADAPTER,
            maxCacheTime = LibraryCacheDuration,
            force = force,
            networkFunc = {
                getLibraryNetwork(steamId)
            }, defaultFunc = {
                CPlayer_GetOwnedGames_Response()
            }
        ).games
    }

    private suspend fun getLibraryNetwork(steamId: SteamID): CPlayer_GetOwnedGames_Response {
        return playerStub.GetOwnedGames(CPlayer_GetOwnedGames_Request(
            steamid = steamId.steamId,
            language = localeService.language,
            skip_unvetted_apps = true,
            include_appinfo = true,
            include_extended_appinfo = true,
            include_free_sub = false,
            include_played_free_games = false
        ))
    }

    private fun key(steamId: SteamID) = "steam.library.${steamId.steamId}"
}
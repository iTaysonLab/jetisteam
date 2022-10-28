package bruhcollective.itaysonlab.jetisteam.repository

import android.util.Log
import bruhcollective.itaysonlab.jetisteam.controllers.ConfigService
import bruhcollective.itaysonlab.jetisteam.controllers.SteamSessionController
import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import bruhcollective.itaysonlab.jetisteam.util.LanguageUtil
import okhttp3.internal.EMPTY_BYTE_ARRAY
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
    private val configService: ConfigService,
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

        val key = key(steamId)
        val cachedTime = configService.long("$key.lastSavedAt", 0)
        val cachedData = configService.bytes(key, EMPTY_BYTE_ARRAY)

        return if (force || cachedData.isEmpty() || System.currentTimeMillis() - cachedTime >= LibraryCacheDuration.inWholeMilliseconds) {
            try {
                getLibraryNetwork(steamId).also { data ->
                    configService.put(key, data.encode())
                    configService.put("$key.lastSavedAt", System.currentTimeMillis())
                }.games
            } catch (e: Exception) {
                e.printStackTrace()
                emptyList()
            }
        } else {
            CPlayer_GetOwnedGames_Response.ADAPTER.decode(cachedData).games
        }
    }

    private suspend fun getLibraryNetwork(steamId: SteamID): CPlayer_GetOwnedGames_Response {
        return playerStub.GetOwnedGames(CPlayer_GetOwnedGames_Request(
            steamid = steamId.steamId,
            language = LanguageUtil.currentLanguage,
            skip_unvetted_apps = true,
            include_appinfo = true,
            include_extended_appinfo = true,
            include_free_sub = false,
            include_played_free_games = false
        ))
    }

    private fun key(steamId: SteamID) = "steam.library.${steamId.steamId}"
}
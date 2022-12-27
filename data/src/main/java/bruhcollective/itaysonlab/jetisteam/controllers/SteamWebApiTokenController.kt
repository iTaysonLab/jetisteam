package bruhcollective.itaysonlab.jetisteam.controllers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton
import kotlin.time.Duration.Companion.hours

@Singleton
class SteamWebApiTokenController @Inject constructor(
    private val cacheService: CacheService,
    @Named("steamOkhttp") private val okHttpClient: OkHttpClient,
    private val steamSessionController: SteamSessionController
) {
    private companion object {
        val REGEX_REPLAY = """
            webapi_token="&quot;(.+?)&quot;"
        """.trimIndent().toRegex()

        val REGEX_ROYALTY = """
            webapi_token&quot;:&quot;(.+?)&quot;
        """.trimIndent().toRegex()
    }

    suspend fun requestWebApiTokenFor(realm: TokenRealm, force: Boolean = false): String {
        val steamId = steamSessionController.steamId().steamId

        return cacheService.primitiveEntry(
            key = "steam.webapi.${steamId}.${realm.name}",
            force = force,
            maxCacheTime = 2.hours,
            networkFunc = {
                withContext(Dispatchers.IO) {
                    okHttpClient.newCall(Request(
                        url = when (realm) {
                            TokenRealm.SteamReplay -> "https://store.steampowered.com/replay/${steamId}/2022"
                            TokenRealm.LoyaltyStore -> "https://store.steampowered.com/points/shop/"
                        }.toHttpUrl()
                    )).execute().use { r ->
                        val content = r.body.string()

                        return@withContext withContext(Dispatchers.Default) {
                            when (realm) {
                                TokenRealm.SteamReplay -> REGEX_REPLAY
                                TokenRealm.LoyaltyStore -> REGEX_ROYALTY
                            }.find(content)!!.groupValues[1]
                        }
                    }
                }
            },
            defaultFunc = { "" },
            cacheFunc = { internalKey ->
                get(ConfigService.Instance.Cache).getString(internalKey, null) ?: error("[webapi:cache] No entry, but no networkFunc called!")
            }
        )
    }

    enum class TokenRealm {
        SteamReplay, LoyaltyStore
    }
}
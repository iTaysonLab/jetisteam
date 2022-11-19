package bruhcollective.itaysonlab.jetisteam.controllers

import bruhcollective.itaysonlab.jetisteam.models.SteamID
import bruhcollective.itaysonlab.jetisteam.repository.UserAccountRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.days

@Singleton
class LocaleService @Inject constructor(
    private val userAccountRepository: UserAccountRepository,
    private val cacheService: CacheService,
    private val steamSessionController: SteamSessionController,
    private val moshi: Moshi,
    private val okHttpClient: OkHttpClient
) {
    private companion object {
        const val DEFAULT_LANGUAGE = "english"
        const val DEFAULT_COUNTRY_CODE = "US"

        val LANG_REGEX = """\('(.+?)'\)""".toRegex()
    }

    // TODO: dynamic
    val language = DEFAULT_LANGUAGE
    var languageDynamic: Map<String, String> = emptyMap()

    suspend fun myCountry(force: Boolean = false) = countryFor(steamId = steamSessionController.steamId(), force = force)

    suspend fun countryFor(steamId: SteamID, force: Boolean = false): String {
        return cacheService.primitiveEntry(
            key = keyCountry(steamId),
            force = force,
            networkFunc = {
                userAccountRepository.getUserCountry(steamId.steamId)
            },
            defaultFunc = { DEFAULT_COUNTRY_CODE },
            cacheFunc = { internalKey ->
                get(ConfigService.Instance.Cache).getString(internalKey, DEFAULT_COUNTRY_CODE) ?: DEFAULT_COUNTRY_CODE
            }
        )
    }

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun localeMap(): Map<String, String> {
        if (languageDynamic.isEmpty()) {
            val adapter = moshi.adapter<Map<String, String>>()

            return cacheService.jsonEntry(
                key = keyLanguage(),
                maxCacheTime = 7.days,
                networkFunc = {
                    withContext(Dispatchers.IO) {
                        okHttpClient.newCall(Request(
                            url = "https://steamcommunity.com/public/javascript/applications/community/localization/shared_${language}-json.js".toHttpUrl()
                        )).execute().use { r ->
                            adapter.fromJson(LANG_REGEX.find(r.body.string().lines().last().replace("\\\\", "\\"))!!.groupValues[1]) ?: emptyMap()
                        }
                    }
                },
                defaultFunc = { emptyMap() },
                adapter = adapter
            ).also { languageDynamic = it }
        } else {
            return languageDynamic
        }
    }

    fun dynamicLocaleFor(id: String): String {
        return languageDynamic[id.removePrefix("#")] ?: id
    }

    suspend fun dynamicLocaleForAwait(id: String): String {
        return localeMap()[id.removePrefix("#")] ?: id
    }

    private fun keyCountry(steamId: SteamID) = "steam.account.${steamId.steamId}.country"
    private fun keyLanguage() = "steam.locale.${language}"
}
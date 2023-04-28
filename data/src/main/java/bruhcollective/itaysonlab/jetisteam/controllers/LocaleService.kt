package bruhcollective.itaysonlab.jetisteam.controllers

import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.ksteam.handlers.Persona
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.days

@Singleton
class LocaleService @Inject constructor(
    private val hostSteamClient: HostSteamClient,
    private val cacheService: CacheService,
    private val okHttpClient: OkHttpClient
) {
    private companion object {
        const val DEFAULT_LANGUAGE = "english"
        val LANG_REGEX = """\('(.+?)'\)""".toRegex()
    }

    // TODO: dynamic
    val language = DEFAULT_LANGUAGE
    var languageDynamic: Map<String, String> = emptyMap()

    fun myCountry(): String {
        return hostSteamClient.client.getHandler<Persona>().currentPersona.value.country
    }

    suspend fun localeMap(): Map<String, String> {
        if (languageDynamic.isEmpty()) {
            val adapter = serializer<Map<String, String>>()

            return cacheService.jsonEntry(
                key = keyLanguage(),
                maxCacheTime = 7.days,
                networkFunc = {
                    withContext(Dispatchers.IO) {
                        okHttpClient.newCall(Request(
                            url = "https://steamcommunity.com/public/javascript/applications/community/localization/shared_${language}-json.js".toHttpUrl()
                        )).execute().use { r ->
                            Json.decodeFromString(adapter, LANG_REGEX.find(r.body.string().lines().last().replace("\\\\", "\\"))!!.groupValues[1]) ?: emptyMap()
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

    private fun keyLanguage() = "steam.locale.${language}"
}
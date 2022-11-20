package bruhcollective.itaysonlab.jetisteam.controllers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.HttpURLConnection
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CdnController @Inject constructor(
    private val okHttpClient: OkHttpClient
) {
    private companion object {
        const val MEDIA_CDN_COMMUNITY_URL = "https://steamcdn-a.akamaihd.net/steamcommunity/public"
        const val MEDIA_CDN_URL = "https://steamcdn-a.akamaihd.net"
        const val COMMUNITY_CDN_ASSET_URL = "https://steamcdn-a.akamaihd.net/steamcommunity/public/assets"
        const val STORE_ICON_BASE_URL = "https://steamcdn-a.akamaihd.net/steam/apps"
        const val COMMUNITY_CDN_URL = "https://steamcommunity-a.akamaihd.net"
    }

    fun publicImage(fileName: String?): String? {
        return "$MEDIA_CDN_COMMUNITY_URL/images/${fileName ?: return null}"
    }

    fun publicItemImage(fileName: String?): String? {
        return "$MEDIA_CDN_COMMUNITY_URL/images/items/${fileName ?: return null}"
    }

    fun buildAppUrl(appId: Int, fileName: String): String {
        return "$STORE_ICON_BASE_URL/$appId/$fileName"
    }

    fun buildCommunityUrl(url: String): String {
        return "$MEDIA_CDN_COMMUNITY_URL/$url"
    }

    fun buildEconomy(url: String): String {
        return "$COMMUNITY_CDN_URL/economy/image/$url"
    }

    fun assetFromTemplate(template: String?, fileName: String?): String? {
        return "$MEDIA_CDN_URL/${template?.replace("\${FILENAME}", fileName ?: return null) ?: return null}"
    }

    suspend fun exists(url: String): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                okHttpClient.newCall(Request(url = url.toHttpUrl(), method = "HEAD")).execute().code == HttpURLConnection.HTTP_OK
            } catch (e: Exception) {
                false
            }
        }
    }
}
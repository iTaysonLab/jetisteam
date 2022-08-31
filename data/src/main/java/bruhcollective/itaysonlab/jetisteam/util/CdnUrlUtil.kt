package bruhcollective.itaysonlab.jetisteam.util

object CdnUrlUtil {
    const val MEDIA_CDN_URL = "https://steamcdn-a.akamaihd.net"

    fun buildPublicUrl(path: String?): String? {
        return "https://cdn.akamai.steamstatic.com/steamcommunity/public/images/${path ?: return null}"
    }

    fun buildAppUrl(appid: Int, file: String): String {
        return "$MEDIA_CDN_URL/steam/apps/$appid/$file"
    }

    fun buildCommunityUrl(url: String): String {
        return "$MEDIA_CDN_URL/steamcommunity/public/$url"
    }

    fun buildEconomy(url: String): String {
        return "https://steamcommunity-a.akamaihd.net/economy/image/$url"
    }

    fun buildAsset(template: String, fileName: String): String {
        return "$MEDIA_CDN_URL/${template.replace("\${FILENAME}", fileName)}"
    }
}
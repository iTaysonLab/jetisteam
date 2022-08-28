package bruhcollective.itaysonlab.jetisteam.util

object CdnUrlUtil {
    fun buildPublicUrl(path: String?): String? {
        return "https://cdn.akamai.steamstatic.com/steamcommunity/public/images/${path ?: return null}"
    }

    // GetCommunityIconURL

    fun buildAppUrl(appid: Int, file: String): String {
        return "https://steamcdn-a.akamaihd.net/steam/apps/$appid/$file"
    }

    fun buildCommunityUrl(url: String): String {
        return "https://steamcdn-a.akamaihd.net/steamcommunity/public/$url"
    }

    fun buildEconomy(url: String): String {
        return "https://steamcommunity-a.akamaihd.net/economy/image/$url"
    }
}
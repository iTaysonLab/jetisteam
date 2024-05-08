package bruhcollective.itaysonlab.cobalt.core.platform

interface PlatformCookieManager {
    fun putCookies(url: String, cookies: List<Pair<String, String>>)
}
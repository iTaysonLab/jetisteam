package bruhcollective.itaysonlab.cobalt.platform

import android.webkit.CookieManager
import bruhcollective.itaysonlab.cobalt.core.platform.PlatformCookieManager

class AndroidPlatformCookieManager: PlatformCookieManager {
    override fun putCookies(url: String, cookies: List<Pair<String, String>>) {
        CookieManager.getInstance().apply {
            for (cookie in cookies) {
                setCookie(url, "${cookie.first}=${cookie.second}")
            }
        }
    }
}
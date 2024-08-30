package bruhcollective.itaysonlab.cobalt.di

import android.webkit.CookieManager
import bruhcollective.itaysonlab.cobalt.core.platform.PlatformCookieManager

class AndroidCookieManager: PlatformCookieManager {
    override fun putCookies(url: String, cookies: List<Pair<String, String>>) {
        CookieManager.getInstance().apply {
            for (cookie in cookies) {
                setCookie(url, "${cookie.first}=${cookie.second}")
            }
        }
    }
}
package bruhcollective.itaysonlab.cobalt.di

import bruhcollective.itaysonlab.cobalt.core.platform.PlatformBrowser
import bruhcollective.itaysonlab.cobalt.core.platform.PlatformCookieManager
import bruhcollective.itaysonlab.cobalt.platform.AndroidPlatformCookieManager
import bruhcollective.itaysonlab.cobalt.platform.AndroidPlatformBrowser
import org.koin.dsl.module

val PlatformModule = module {
    single<PlatformBrowser> {
        AndroidPlatformBrowser(context = get())
    }

    single<PlatformCookieManager> {
        AndroidPlatformCookieManager()
    }
}
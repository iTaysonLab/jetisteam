package bruhcollective.itaysonlab.cobalt.di

import bruhcollective.itaysonlab.cobalt.core.platform.PlatformBrowser
import bruhcollective.itaysonlab.cobalt.core.platform.PlatformCookieManager
import org.koin.dsl.module

val PlatformModule = module {
    single<PlatformBrowser> {
        AndroidPlatformBrowser(context = get())
    }

    single<PlatformCookieManager> {
        AndroidCookieManager()
    }
}
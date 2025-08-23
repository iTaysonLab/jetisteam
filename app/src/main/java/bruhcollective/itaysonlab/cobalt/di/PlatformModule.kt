package bruhcollective.itaysonlab.cobalt.di

import bruhcollective.itaysonlab.cobalt.core.platform.PlatformBrowser
import bruhcollective.itaysonlab.cobalt.core.platform.PlatformCookieManager
import bruhcollective.itaysonlab.cobalt.platform.AndroidPlatformBrowser
import bruhcollective.itaysonlab.cobalt.platform.AndroidPlatformCookieManager
import dev.icerock.moko.permissions.PermissionsController
import org.koin.dsl.module

val PlatformModule = module {
    single<PlatformBrowser> {
        AndroidPlatformBrowser(context = get())
    }

    single<PlatformCookieManager> {
        AndroidPlatformCookieManager()
    }

    single<PermissionsController> {
        PermissionsController(applicationContext = get())
    }
}
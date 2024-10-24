package bruhcollective.itaysonlab.cobalt

import android.app.Application
import bruhcollective.itaysonlab.cobalt.core.ksteam.KsteamModule
import bruhcollective.itaysonlab.cobalt.di.PlatformModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CobaltApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // RecomposeLoggerConfig.isEnabled = true
        // RecomposeHighlighterConfig.isEnabled = true

        startKoin {
            androidContext(applicationContext)
            modules(PlatformModule, KsteamModule)
        }
    }
}
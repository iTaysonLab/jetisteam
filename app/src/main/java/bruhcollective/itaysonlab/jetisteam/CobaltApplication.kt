package bruhcollective.itaysonlab.jetisteam

import android.app.Application
import bruhcollective.itaysonlab.cobalt.core.ksteam.KsteamModule
import bruhcollective.itaysonlab.jetisteam.di.PlatformModule
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
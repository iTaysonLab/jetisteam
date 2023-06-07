package bruhcollective.itaysonlab.jetisteam

import android.app.Application
import bruhcollective.itaysonlab.cobalt.core.ksteam.KsteamModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class CobaltApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(applicationContext)
            modules(KsteamModule)
        }
    }
}
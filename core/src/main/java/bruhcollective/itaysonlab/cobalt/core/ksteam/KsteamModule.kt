package bruhcollective.itaysonlab.cobalt.core.ksteam

import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val KsteamModule = module(createdAtStart = true) {
    single {
        SteamClient(applicationContext = androidContext())
    }
}
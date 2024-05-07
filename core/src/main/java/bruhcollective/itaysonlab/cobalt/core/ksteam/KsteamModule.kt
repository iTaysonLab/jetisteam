package bruhcollective.itaysonlab.cobalt.core.ksteam

import bruhcollective.itaysonlab.cobalt.core.CobaltDispatchers
import bruhcollective.itaysonlab.ksteam.ExtendedSteamClient
import kotlinx.coroutines.Dispatchers
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val KsteamModule = module(createdAtStart = true) {
    single<CobaltDispatchers> {
        object: CobaltDispatchers {
            override val main = Dispatchers.Main
            override val io = Dispatchers.IO
        }
    }

    single<SteamClient> {
        SteamClient(applicationContext = androidContext())
    }

    single<ExtendedSteamClient> {
        get<SteamClient>().ksteam
    }
}
package bruhcollective.itaysonlab.cobalt.core.ksteam

import bruhcollective.itaysonlab.cobalt.core.CobaltDispatchers
import kotlinx.coroutines.CoroutineDispatcher
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

    single {
        SteamClient(applicationContext = androidContext())
    }

    single {
        get<SteamClient>().ksteam
    }
}
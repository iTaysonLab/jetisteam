package bruhcollective.itaysonlab.cobalt.core.ksteam

import com.tencent.mmkv.MMKV
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val KsteamModule = module(createdAtStart = true) {
    single {
        MMKV.initialize(androidContext())
        MMKV.defaultMMKV()
    }

    single {
        SteamClient(applicationContext = androidContext(), mmkv = get())
    }
}
package bruhcollective.itaysonlab.microapp.steam_wrapped.di

import bruhcollective.itaysonlab.microapp.core.MicroappEntry
import bruhcollective.itaysonlab.microapp.core.di.MicroappEntryKey
import bruhcollective.itaysonlab.microapp.steam_wrapped.SteamWrappedMicroapp
import bruhcollective.itaysonlab.microapp.steam_wrapped.SteamWrappedMicroappImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface SteamWrappedMicroappModule {
    @Singleton
    @Binds
    @IntoMap
    @MicroappEntryKey(value = SteamWrappedMicroapp::class)
    fun steamWrappedEntry(entry: SteamWrappedMicroappImpl): MicroappEntry
}
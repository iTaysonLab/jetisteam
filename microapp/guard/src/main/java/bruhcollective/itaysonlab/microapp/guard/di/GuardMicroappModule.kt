package bruhcollective.itaysonlab.microapp.guard.di

import bruhcollective.itaysonlab.microapp.core.MicroappEntry
import bruhcollective.itaysonlab.microapp.core.di.MicroappEntryKey
import bruhcollective.itaysonlab.microapp.guard.GuardMicroapp
import bruhcollective.itaysonlab.microapp.guard.GuardMicroappImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface GuardMicroappModule {
    @Singleton
    @Binds
    @IntoMap
    @MicroappEntryKey(value = GuardMicroapp::class)
    fun guardEntry(entry: GuardMicroappImpl): MicroappEntry
}
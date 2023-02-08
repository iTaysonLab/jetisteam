package bruhcollective.itaysonlab.microapp.library.di

import bruhcollective.itaysonlab.microapp.core.MicroappEntry
import bruhcollective.itaysonlab.microapp.core.di.MicroappEntryKey
import bruhcollective.itaysonlab.microapp.library.LibraryMicroapp
import bruhcollective.itaysonlab.microapp.library.LibraryMicroappImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface LibraryMicroappModule {
    @Singleton
    @Binds
    @IntoMap
    @MicroappEntryKey(value = LibraryMicroapp::class)
    fun guardEntry(entry: LibraryMicroappImpl): MicroappEntry
}
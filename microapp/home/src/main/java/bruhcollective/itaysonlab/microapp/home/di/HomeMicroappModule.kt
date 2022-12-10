package bruhcollective.itaysonlab.microapp.home.di

import bruhcollective.itaysonlab.microapp.core.MicroappEntry
import bruhcollective.itaysonlab.microapp.core.di.MicroappEntryKey
import bruhcollective.itaysonlab.microapp.home.HomeMicroapp
import bruhcollective.itaysonlab.microapp.home.HomeMicroappImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface HomeMicroappModule {
    @Singleton
    @Binds
    @IntoMap
    @MicroappEntryKey(value = HomeMicroapp::class)
    fun homePageEntry(entry: HomeMicroappImpl): MicroappEntry
}
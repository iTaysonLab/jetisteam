package bruhcollective.itaysonlab.microapp.profile.di

import bruhcollective.itaysonlab.microapp.core.MicroappEntry
import bruhcollective.itaysonlab.microapp.core.di.MicroappEntryKey
import bruhcollective.itaysonlab.microapp.profile.ProfileMicroapp
import bruhcollective.itaysonlab.microapp.profile.ProfileMicroappImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface ProfileMicroappModule {
    @Singleton
    @Binds
    @IntoMap
    @MicroappEntryKey(value = ProfileMicroapp::class)
    fun profileEntry(entry: ProfileMicroappImpl): MicroappEntry
}
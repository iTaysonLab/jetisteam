package bruhcollective.itaysonlab.microapp.auth.di

import bruhcollective.itaysonlab.microapp.auth.AuthMicroapp
import bruhcollective.itaysonlab.microapp.auth.AuthMicroappImpl
import bruhcollective.itaysonlab.microapp.core.MicroappEntry
import bruhcollective.itaysonlab.microapp.core.di.MicroappEntryKey
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AuthMicroappModule {
    @Singleton
    @Binds
    @IntoMap
    @MicroappEntryKey(value = AuthMicroapp::class)
    fun authEntry(entry: AuthMicroappImpl): MicroappEntry
}
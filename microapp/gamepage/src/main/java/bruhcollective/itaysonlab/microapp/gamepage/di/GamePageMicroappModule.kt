package bruhcollective.itaysonlab.microapp.gamepage.di

import bruhcollective.itaysonlab.microapp.core.MicroappEntry
import bruhcollective.itaysonlab.microapp.core.di.MicroappEntryKey
import bruhcollective.itaysonlab.microapp.gamepage.GamePageMicroapp
import bruhcollective.itaysonlab.microapp.gamepage.GamePageMicroappImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface GamePageMicroappModule {
    @Singleton
    @Binds
    @IntoMap
    @MicroappEntryKey(value = GamePageMicroapp::class)
    fun gamePageEntry(entry: GamePageMicroappImpl): MicroappEntry
}
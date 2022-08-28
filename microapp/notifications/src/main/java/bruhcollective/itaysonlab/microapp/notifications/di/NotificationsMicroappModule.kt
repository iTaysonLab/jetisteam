package bruhcollective.itaysonlab.microapp.notifications.di

import bruhcollective.itaysonlab.microapp.core.MicroappEntry
import bruhcollective.itaysonlab.microapp.core.di.MicroappEntryKey
import bruhcollective.itaysonlab.microapp.notifications.NotificationsMicroapp
import bruhcollective.itaysonlab.microapp.notifications.NotificationsMicroappImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface NotificationsMicroappModule {
    @Singleton
    @Binds
    @IntoMap
    @MicroappEntryKey(value = NotificationsMicroapp::class)
    fun notificationsEntry(entry: NotificationsMicroappImpl): MicroappEntry
}
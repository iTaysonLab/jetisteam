package bruhcollective.itaysonlab.jetisteam.di

import bruhcollective.itaysonlab.jetisteam.HostSteamClient
import bruhcollective.itaysonlab.ksteam.handlers.Account
import bruhcollective.itaysonlab.ksteam.handlers.Library
import bruhcollective.itaysonlab.ksteam.handlers.News
import bruhcollective.itaysonlab.ksteam.handlers.Persona
import bruhcollective.itaysonlab.ksteam.handlers.Pics
import bruhcollective.itaysonlab.ksteam.handlers.Player
import bruhcollective.itaysonlab.ksteam.handlers.Store
import bruhcollective.itaysonlab.ksteam.handlers.account
import bruhcollective.itaysonlab.ksteam.handlers.library
import bruhcollective.itaysonlab.ksteam.handlers.news
import bruhcollective.itaysonlab.ksteam.handlers.persona
import bruhcollective.itaysonlab.ksteam.handlers.pics
import bruhcollective.itaysonlab.ksteam.handlers.player
import bruhcollective.itaysonlab.ksteam.handlers.store
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object KsteamModule {
    @Provides
    @Singleton
    fun providePlayer(hostSteamClient: HostSteamClient): Player = hostSteamClient.client.player

    @Provides
    @Singleton
    fun provideStore(hostSteamClient: HostSteamClient): Store = hostSteamClient.client.store

    @Provides
    @Singleton
    fun providePersona(hostSteamClient: HostSteamClient): Persona = hostSteamClient.client.persona

    @Provides
    @Singleton
    fun provideNews(hostSteamClient: HostSteamClient): News = hostSteamClient.client.news

    @Provides
    @Singleton
    fun provideLibrary(hostSteamClient: HostSteamClient): Library = hostSteamClient.client.library

    @Provides
    @Singleton
    fun providePics(hostSteamClient: HostSteamClient): Pics = hostSteamClient.client.pics

    @Provides
    @Singleton
    fun provideAccount(hostSteamClient: HostSteamClient): Account = hostSteamClient.client.account
}
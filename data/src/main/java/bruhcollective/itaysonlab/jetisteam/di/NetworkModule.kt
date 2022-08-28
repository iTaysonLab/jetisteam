package bruhcollective.itaysonlab.jetisteam.di

import bruhcollective.itaysonlab.jetisteam.controllers.SteamAuthInterceptor
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcChannel
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkhttp() = OkHttpClient()

    @Provides
    @Singleton
    @Named("steamOkhttp")
    fun provideSteamOkhttp(
        steamAuthInterceptor: SteamAuthInterceptor
    ) = OkHttpClient.Builder().addInterceptor(steamAuthInterceptor).build()

    @Provides
    @Singleton
    @Named("neutralRpcChannel")
    fun provideNeturalRpcChannel(
        okHttpClient: OkHttpClient
    ) = SteamRpcChannel(okHttpClient)

    @Provides
    @Singleton
    fun provideRetrofit(
        @Named("steamOkhttp") okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://steamcommunity.com/")
        .build()

    @Provides
    @Singleton
    @Named("steamPoweredRetrofit")
    fun provideStoreSteamPoweredRetrofit(
        @Named("steamPoweredRetrofit") okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://store.steampowered.com/")
        .build()
}
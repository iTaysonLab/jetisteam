package bruhcollective.itaysonlab.jetisteam.di

import bruhcollective.itaysonlab.jetisteam.controllers.SteamAuthInterceptor
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcProcessor
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
        okHttpClient: OkHttpClient
    ) = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create())
        .baseUrl("https://store.steampowered.com/")
        .build()

    @Provides
    @Singleton
    @Named("neutralRpcClient")
    fun provideNeturalRpcClient(
        @Named("neutralRpcProcessor") processor: SteamRpcProcessor
    ) = SteamRpcClient(processor)
    @Provides
    @Singleton
    @Named("neutralRpcProcessor")
    fun provideNeturalRpcProcessor(
        okHttpClient: OkHttpClient
    ) = SteamRpcProcessor(okHttpClient)
}
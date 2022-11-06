package bruhcollective.itaysonlab.jetisteam.di

import bruhcollective.itaysonlab.jetisteam.controllers.SteamAuthInterceptor
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcClient
import bruhcollective.itaysonlab.jetisteam.rpc.SteamRpcProcessor
import com.squareup.moshi.Moshi
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
    fun provideMoshi() = Moshi.Builder().build()

    @Provides
    @Singleton
    @Named("steamOkhttp")
    fun provideSteamOkhttp(
        steamAuthInterceptor: SteamAuthInterceptor
    ) = OkHttpClient.Builder().addInterceptor(steamAuthInterceptor).build()

    @Provides
    @Singleton
    @Named("steamCommunityRetrofit")
    fun provideSteamCommunityRetrofit(
        @Named("steamOkhttp") okHttpClient: OkHttpClient,
        moshi: Moshi
    ) = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl("https://steamcommunity.com/")
        .build()

    @Provides
    @Singleton
    @Named("steamPoweredRetrofit")
    fun provideSteamStoreRetrofit(
        @Named("steamOkhttp") okHttpClient: OkHttpClient,
        moshi: Moshi
    ) = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl("https://store.steampowered.com/")
        .build()

    @Provides
    @Singleton
    @Named("steamApiRetrofit")
    fun provideSteamApiRetrofit(
        @Named("steamOkhttp") okHttpClient: OkHttpClient,
        moshi: Moshi
    ) = Retrofit.Builder()
        .client(okHttpClient)
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .baseUrl("https://api.steampowered.com/")
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
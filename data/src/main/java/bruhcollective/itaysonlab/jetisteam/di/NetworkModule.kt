package bruhcollective.itaysonlab.jetisteam.di

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
    @Named("steamCommunityRetrofit")
    fun provideSteamCommunityRetrofit(
        okHttpClient: OkHttpClient,
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
}
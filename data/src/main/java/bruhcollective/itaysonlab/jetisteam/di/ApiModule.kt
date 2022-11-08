package bruhcollective.itaysonlab.jetisteam.di

import bruhcollective.itaysonlab.jetisteam.service.ApiService
import bruhcollective.itaysonlab.jetisteam.service.GameService
import bruhcollective.itaysonlab.jetisteam.service.MiniprofileService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.create
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApiModule {
    @Provides
    @Singleton
    fun provideMiniprofileService(@Named("steamCommunityRetrofit") retrofit: Retrofit) = retrofit.create<MiniprofileService>()

    @Provides
    @Singleton
    fun provideApiService(@Named("steamCommunityRetrofit") retrofit: Retrofit) = retrofit.newBuilder().baseUrl("https://api.steampowered.com/").build().create<ApiService>()

    @Provides
    @Singleton
    fun provideGameService(@Named("steamPoweredRetrofit") retrofit: Retrofit) = retrofit.create<GameService>()
}
package bruhcollective.itaysonlab.jetisteam.service

import bruhcollective.itaysonlab.jetisteam.models.*
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("ISteamUserOAuth/GetUserSummaries/v2/")
    suspend fun resolvePlayers(
        @Query("steamids") ids: String
    ): Players

    @POST("ITwoFactorService/QueryTime/v0001")
    suspend fun queryTime(
        @Query("steamid") id: String = ""
    ): QueryTimeResponse
}
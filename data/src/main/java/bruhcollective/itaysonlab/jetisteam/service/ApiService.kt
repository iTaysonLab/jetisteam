package bruhcollective.itaysonlab.jetisteam.service

import bruhcollective.itaysonlab.jetisteam.models.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/ISteamUserOAuth/GetUserSummaries/v2/")
    suspend fun resolvePlayers(
        @Query("steamids") ids: String
    ): Players
}
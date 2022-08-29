package bruhcollective.itaysonlab.jetisteam.service

import bruhcollective.itaysonlab.jetisteam.models.GameDetails
import retrofit2.http.GET
import retrofit2.http.Query

interface GameService {
    @GET("/api/libraryappdetails/")
    suspend fun getGameDetails(
        @Query("appid") appId: String
    ): GameDetails
}
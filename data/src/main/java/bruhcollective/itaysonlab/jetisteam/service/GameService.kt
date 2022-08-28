package bruhcollective.itaysonlab.jetisteam.service

import bruhcollective.itaysonlab.jetisteam.models.GameDetails
import retrofit2.http.GET

interface GameService {
    @GET("/api/libraryappdetails/?appid={appId}")
    suspend fun getGameDetails(
        appId: Int
    ): GameDetails
}
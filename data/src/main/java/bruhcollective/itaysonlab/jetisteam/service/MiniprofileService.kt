package bruhcollective.itaysonlab.jetisteam.service

import bruhcollective.itaysonlab.jetisteam.models.Miniprofile
import retrofit2.http.GET
import retrofit2.http.Path

interface MiniprofileService {
    @GET("/miniprofile/{accountId}/json")
    suspend fun getMiniprofile(
        @Path("accountId") accountId: Long
    ): Miniprofile
}
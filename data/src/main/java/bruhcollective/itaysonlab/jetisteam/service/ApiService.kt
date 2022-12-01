package bruhcollective.itaysonlab.jetisteam.service

import bruhcollective.itaysonlab.jetisteam.models.*
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
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

    @JsonClass(generateAdapter = true)
    class QueryTimeResponse(
        val response: QueryTimeData
    )

    @JsonClass(generateAdapter = true)
    class QueryTimeData(
        @Json(name = "server_time") val serverTime: Long,
        @Json(name = "allow_correction") val allowCorrection: Boolean?,
    )
}
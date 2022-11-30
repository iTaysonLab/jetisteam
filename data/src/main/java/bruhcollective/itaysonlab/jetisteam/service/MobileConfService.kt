package bruhcollective.itaysonlab.jetisteam.service

import bruhcollective.itaysonlab.jetisteam.models.MobileConfGetList
import bruhcollective.itaysonlab.jetisteam.models.MobileConfResult
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface MobileConfService {
    @GET("/getlist")
    suspend fun getConfirmations(
        @Query("p") platform: String,
        @Query("a") steamId: Long,
        @Query("t") timestamp: Long,
        @Query("k") signature: String,
        @Query("m") m: String = "react",
        @Query("tag") tag: String = "list",
    ): MobileConfGetList

    @POST("/ajaxop")
    suspend fun runOperation(
        @Query("p") platform: String,
        @Query("a") steamId: Long,
        @Query("t") timestamp: Long,
        @Query("m") m: String = "react",
        @Query("tag") tag: String, // reject/accept
        @Query("op") operation: String, // cancel/allow
        @Query("cid") cid: String,
        @Query("ck") ck: String,
        @Query("k") signature: String,
    ): MobileConfResult
}
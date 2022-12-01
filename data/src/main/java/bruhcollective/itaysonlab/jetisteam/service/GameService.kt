package bruhcollective.itaysonlab.jetisteam.service

import bruhcollective.itaysonlab.jetisteam.models.*
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GameService {
    @GET("api/libraryappdetails/")
    suspend fun getGameDetails(
        @Query("appid") appId: String
    ): GameDetails

    @GET("api/appdetails")
    suspend fun getGameDetails(
        @Query("appids") appId: String,
        @Query("l") language: String,
        @Query("cc") country: String,
    ): Map<String, GameFullDetails>

    @GET("api/appuserdetails")
    suspend fun getGameCompat(
        @Query("appids") appIds: String
    ): Map<String, GameCompatDetails>

    @GET("saleaction/ajaxgetdeckappcompatibilityreport")
    suspend fun getDeckReport(
        @Query("nAppID") appId: String,
        @Query("l") language: String,
        @Query("cc") country: String,
    ): SteamDeckSupportReportWrap

    @GET("appreviews/{appId}")
    suspend fun getReviews(
        @Path("appId") appId: String,
        @Query("l") language: String,
        @Query("language") reviewLanguages: String, // can be "english,ukrainian,czech" for example
        @Query("filter") filter: String = "summary",
        @Query("json") renderAsJson: String = "1",
        @Query("cursor") sqlCursor: String = "*",
        @Query("day_range") dayRange: String = "30",
        @Query("start_date") startDate: String = "-1",
        @Query("end_date") endDate: String = "-1",
        @Query("date_range_type") dateRangeType: String = "all",
        @Query("review_type") reviewType: String = "all",
        @Query("purchase_type") purchaseType: String = "all",
        @Query("playtime_filter_min") minPlaytime: String = "0",
        @Query("playtime_filter_max") maxPlaytime: String = "0",
        @Query("filter_offtopic_activity") filterOfftopic: String = "1",
    ): Reviews
}
package bruhcollective.itaysonlab.jetisteam.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class QueryTimeResponse(
    val response: QueryTimeData
)

@JsonClass(generateAdapter = true)
class QueryTimeData(
    @Json(name = "server_time") val serverTime: Long,
    @Json(name = "allow_correction") val allowCorrection: Boolean?,
)
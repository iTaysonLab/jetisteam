package bruhcollective.itaysonlab.jetisteam.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class QueryTimeResponse(
    val response: QueryTimeData
)

@Serializable
class QueryTimeData(
    @SerialName("server_time") val serverTime: Long,
    @SerialName("allow_correction") val allowCorrection: Boolean?,
)
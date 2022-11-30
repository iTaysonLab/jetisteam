package bruhcollective.itaysonlab.jetisteam.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class MobileConfResult(
    val success: Boolean,
)

@JsonClass(generateAdapter = true)
class MobileConfGetList(
    val success: Boolean,
    val conf: List<MobileConfTradeItem>
)


@JsonClass(generateAdapter = true)
class MobileConfTradeItem(
    val type: Int,
    @Json(name = "type_name") val typeName: String,
    val id: String,
    @Json(name = "creator_id") val creatorId: String,
    val nonce: String,
    @Json(name = "creation_time") val creationTime: Long,
    @Json(name = "cancel") val cancelButtonText: String,
    @Json(name = "accept") val acceptButtonText: String,
    val icon: String,
    val multi: Boolean,
    val headline: String,
    val summary: List<String>,
)
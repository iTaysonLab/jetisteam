package bruhcollective.itaysonlab.jetisteam.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class SteamDeckSupportReportWrap(
    val success: Int,
    val results: SteamDeckSupportReport
)

@JsonClass(generateAdapter = true)
class SteamDeckSupportReport(
    @Json(name = "appid") val appId: Int,
    @Json(name = "resolved_category") val resolvedCategory: Int,
    @Json(name = "resolved_items") val resolvedItems: List<SupportItem>
) {
    @Transient val category = SteamDeckSupport.values().first { it.enumInt == resolvedCategory }

    @JsonClass(generateAdapter = true)
    class SupportItem(
        @Json(name = "display_type") val type: Int,
        @Json(name = "loc_token") val stringRef: String,
    ) {
        @Transient val displayType = SteamDeckTestResult.values().first { it.enumInt == type }
    }
}

enum class SteamDeckSupport(val enumInt: Int) {
    Unsupported(1),
    Playable(2),
    Verified(3)
}

enum class SteamDeckTestResult(val enumInt: Int) {
    Note(1),
    Failed(2),
    Info(3),
    Verified(4)
}
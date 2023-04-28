package bruhcollective.itaysonlab.jetisteam.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class SteamDeckSupportReportWrap(
    val success: Int,
    val results: SteamDeckSupportReport
)

@Serializable
class SteamDeckSupportReport(
    @SerialName("appid") val appId: Int,
    @SerialName("resolved_category") val resolvedCategory: Int,
    @SerialName("resolved_items") val resolvedItems: List<SupportItem>
) {
    @delegate:Transient
    val category by lazy {
        SteamDeckSupport.values().firstOrNull { it.enumInt == resolvedCategory } ?: SteamDeckSupport.Unknown
    }

    @Serializable
    class SupportItem(
        @SerialName("display_type") val type: Int,
        @SerialName("loc_token") val stringRef: String,
    ) {
        @delegate:Transient
        val displayType by lazy {
            SteamDeckTestResult.values().firstOrNull { it.enumInt == type } ?: SteamDeckTestResult.Note
        }
    }
}

enum class SteamDeckSupport(val enumInt: Int) {
    Unknown(0),
    Unsupported(1),
    Playable(2),
    Verified(3),
}

enum class SteamDeckTestResult(val enumInt: Int) {
    Note(1),
    Failed(2),
    Info(3),
    Verified(4)
}
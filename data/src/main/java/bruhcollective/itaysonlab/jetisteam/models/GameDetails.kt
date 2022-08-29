package bruhcollective.itaysonlab.jetisteam.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class GameDetails(
    val status: Int,
    val appid: String,
    val name: String,
    @Json(name = "rgDevelopers") val developers: List<NameUrlRelation>,
    @Json(name = "rgFranchises") val franchises: List<NameUrlRelation>,
    @Json(name = "rgPublishers") val publishers: List<NameUrlRelation>,
    @Json(name = "rgSocialMedia") val socialMedia: Map<String, SocialMediaRelation>,
    @Json(name = "strSnippet") val snippet: String,
    @Json(name = "strFullDescription") val fullDescription: String,
)

@JsonClass(generateAdapter = true)
class NameUrlRelation(
    val name: String,
    val url: String?
)

@JsonClass(generateAdapter = true)
class SocialMediaRelation(
    val username: String,
    val url: String,
    @Json(name = "is_valid") val isValid: Int
)
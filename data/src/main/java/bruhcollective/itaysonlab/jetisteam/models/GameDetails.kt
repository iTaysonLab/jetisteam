package bruhcollective.itaysonlab.jetisteam.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GameDetails(
    val status: Int,
    val appid: String,
    val name: String,
    @SerialName("rgDevelopers") val developers: List<NameUrlRelation>?,
    @SerialName("rgFranchises") val franchises: List<NameUrlRelation>?,
    @SerialName("rgPublishers") val publishers: List<NameUrlRelation>?,
    // @SerialName("rgSocialMedia") val socialMedia: Map<String, SocialMediaRelation>,
    @SerialName("strSnippet") val snippet: String,
    @SerialName("strFullDescription") val fullDescription: String,
)

@Serializable
class NameUrlRelation(
    val name: String,
    val url: String?
)

@Serializable
class SocialMediaRelation(
    val username: String,
    val url: String,
    @SerialName("is_valid") val isValid: Int
)
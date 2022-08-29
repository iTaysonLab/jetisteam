package bruhcollective.itaysonlab.jetisteam.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GameDetails(
    val appid: String,
    val name: String,
    @Json(name = "rgDevelopers") val developers: List<Developer>,
    @Json(name = "rgFranchises") val franchises: List<Franchise>,
    @Json(name = "rgPublishers") val publishers: List<Publisher>,
    val status: Int,
    @Json(name = "strFullDescription") val fullDescription: String,
    val strSnippet: String
)

@JsonClass(generateAdapter = true)
data class Developer(
    val name: String,
    val url: String
)

@JsonClass(generateAdapter = true)
data class Franchise(
    val name: String,
    val url: String
)

@JsonClass(generateAdapter = true)
data class Publisher(
    val name: String,
    val url: String
)
package bruhcollective.itaysonlab.jetisteam.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class GameFullDetails(
    val success: Boolean,
    val data: GameFullDetailsData?
)

@JsonClass(generateAdapter = true)
class GameFullDetailsData(
    val type: String,
    val name: String,
    @Json(name = "steam_appid") val appId: Int,
    @Json(name = "controller_support") val controllerSupport: String?,
    val dlc: List<Int>?,
    @Json(name = "short_description") val shortDescription: String,
    @Json(name = "detailed_description") val fullDescription: String,
    @Json(name = "about_the_game") val aboutDescription: String?,
    @Json(name = "reviews") val reviews: String?,
    @Json(name = "legal_notice") val legalNotice: String?,
    @Json(name = "supported_languages") val supportedLanguages: String,
    val developers: List<String>,
    val publishers: List<String>,
    @Json(name = "header_image") val headerImage: String,
    @Json(name = "background") val backgroundImage: String,
    @Json(name = "background_raw") val rawBackgroundImage: String,
    val website: String?,
    /*@Json(name = "pc_requirements") val pcRequirements: Requirements,
    @Json(name = "linux_requirements") val linuxRequirements: Requirements,
    @Json(name = "mac_requirements") val macRequirements: Requirements,*/
    val platforms: Platforms,
    val categories: List<KeyValue>,
    val genres: List<KeyValue>,
    @Json(name = "drm_notice") val drmNotice: String?,
    @Json(name = "release_date") val releaseDate: ReleaseDate,
    @Json(name = "support_info") val supportInfo: SupportInfo,
    val screenshots: List<Screenshot>,
    val movies: List<Movie>?,
    val metacritic: MetacriticLocator?
) {
    @JsonClass(generateAdapter = true)
    class Requirements(
        val minimum: String,
        val recommended: String?
    )

    @JsonClass(generateAdapter = true)
    class Platforms(
        val windows: Boolean,
        val mac: Boolean,
        val linux: Boolean,
    )

    @JsonClass(generateAdapter = true)
    class KeyValue(
        val id: Int,
        val description: String
    )

    @JsonClass(generateAdapter = true)
    class ReleaseDate(
        @Json(name = "coming_soon") val comingSoon: Boolean,
        val date: String
    )

    @JsonClass(generateAdapter = true)
    class SupportInfo(
        val url: String,
        val email: String,
    )

    @JsonClass(generateAdapter = true)
    class Screenshot(
        val id: Int,
        @Json(name = "path_thumbnail") val thumbnail: String,
        @Json(name = "path_full") val url: String,
    )

    @JsonClass(generateAdapter = true)
    class Movie(
        val id: Long,
        val name: String,
        val webm: MovieLocator,
        val mp4: MovieLocator,
    ) {
        @JsonClass(generateAdapter = true)
        class MovieLocator(
            @Json(name = "480") val sd: String,
            @Json(name = "max") val hd: String
        )
    }

    @JsonClass(generateAdapter = true)
    class MetacriticLocator(
        val score: Int,
        val url: String
    )
}
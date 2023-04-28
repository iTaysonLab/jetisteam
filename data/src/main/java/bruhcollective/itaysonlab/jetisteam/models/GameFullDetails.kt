package bruhcollective.itaysonlab.jetisteam.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
class GameFullDetails(
    val success: Boolean,
    val data: GameFullDetailsData?
)

@Serializable
class GameFullDetailsData(
    val type: String,
    val name: String,
    @SerialName("steam_appid") val appId: Int,
    @SerialName("controller_support") val controllerSupport: String?,
    val dlc: List<Int>?,
    @SerialName("short_description") val shortDescription: String,
    @SerialName("detailed_description") val fullDescription: String,
    @SerialName("about_the_game") val aboutDescription: String?,
    @SerialName("reviews") val reviews: String?,
    @SerialName("legal_notice") val legalNotice: String?,
    @SerialName("supported_languages") val supportedLanguages: String,
    val developers: List<String>,
    val publishers: List<String>,
    @SerialName("header_image") val headerImage: String,
    @SerialName("background") val backgroundImage: String,
    @SerialName("background_raw") val rawBackgroundImage: String,
    val website: String?,
    /*@SerialName("pc_requirements") val pcRequirements: Requirements,
    @SerialName("linux_requirements") val linuxRequirements: Requirements,
    @SerialName("mac_requirements") val macRequirements: Requirements,*/
    val platforms: Platforms,
    val categories: List<KeyValue>,
    val genres: List<KeyValue>,
    @SerialName("drm_notice") val drmNotice: String?,
    @SerialName("release_date") val releaseDate: ReleaseDate,
    @SerialName("support_info") val supportInfo: SupportInfo,
    val screenshots: List<Screenshot>,
    val movies: List<Movie>?,
    val metacritic: MetacriticLocator?
) {
    @Serializable
    class Requirements(
        val minimum: String,
        val recommended: String?
    )

    @Serializable
    class Platforms(
        val windows: Boolean,
        val mac: Boolean,
        val linux: Boolean,
    )

    @Serializable
    class KeyValue(
        val id: Int,
        val description: String
    )

    @Serializable
    class ReleaseDate(
        @SerialName("coming_soon") val comingSoon: Boolean,
        val date: String
    )

    @Serializable
    class SupportInfo(
        val url: String,
        val email: String,
    )

    @Serializable
    class Screenshot(
        val id: Int,
        @SerialName("path_thumbnail") val thumbnail: String,
        @SerialName("path_full") val url: String,
    )

    @Serializable
    class Movie(
        val id: Long,
        val name: String,
        val webm: MovieLocator,
        val mp4: MovieLocator,
    ) {
        @Serializable
        class MovieLocator(
            @SerialName("480") val sd: String,
            @SerialName("max") val hd: String
        )
    }

    @Serializable
    class MetacriticLocator(
        val score: Int,
        val url: String
    )
}
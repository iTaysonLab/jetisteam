package bruhcollective.itaysonlab.cobalt.news.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface NewsfeedType {
    @Serializable
    @SerialName("everything")
    data object Everything: NewsfeedType

    @Serializable
    @SerialName("upcoming")
    data object Upcoming: NewsfeedType

    @Serializable
    @SerialName("news")
    data object News: NewsfeedType

    @Serializable
    @SerialName("activity")
    data object Activity: NewsfeedType

    //

    @Serializable
    @SerialName("featured")
    data object Featured: NewsfeedType

    @Serializable
    @SerialName("press")
    data object Press: NewsfeedType

    @Serializable
    @SerialName("steam")
    data object Steam: NewsfeedType
}
package bruhcollective.itaysonlab.microapp.profile

import bruhcollective.itaysonlab.microapp.core.BottomNavigationCapable
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import bruhcollective.itaysonlab.microapp.core.NestedMicroappEntry
import bruhcollective.itaysonlab.microapp.profile.ProfileMicroappImpl.InternalRoutes.ARG_ID
import bruhcollective.itaysonlab.microapp.profile.ProfileMicroappImpl.InternalRoutes.ARG_MY_PROFILE

abstract class ProfileMicroapp: NestedMicroappEntry, BottomNavigationCapable {
    override val microappRoute = "profile/{$ARG_ID}"

    override val arguments: List<NamedNavArgument> =
        listOf(
            navArgument(ARG_ID) {
                type = NavType.StringType
                nullable = false
            }
        )

    fun myProfileDestination() =
        ProfileMicroappImpl.InternalRoutes.Profile.replace("{$ARG_ID}", ARG_MY_PROFILE)

    fun profileDestination(steamId: Long) =
        ProfileMicroappImpl.InternalRoutes.Profile.replace("{$ARG_ID}", steamId.toString())

    fun friendsDestination(steamId: Long) =
        ProfileMicroappImpl.InternalRoutes.Friends.replace("{$ARG_ID}", steamId.toString())
}
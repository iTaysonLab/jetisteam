package bruhcollective.itaysonlab.microapp.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import bruhcollective.itaysonlab.microapp.core.*
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments

abstract class ProfileMicroapp: NestedMicroappEntry, BottomNavigationCapable {
    override val graphRoute = Routes.NavGraph
    override val startDestination = Routes.Profile.url

    override val bottomNavigationEntry = NavigationEntry(
        route = Routes.NavGraph,
        name = R.string.profile,
        icon = { Icons.Rounded.Person }
    )

    fun myProfileDestination() =
        Routes.Profile.mapArgs(mapOf(
            CommonArguments.SteamId to 0L
        ))

    fun profileDestination(steamId: Long) =
        Routes.Profile.mapArgs(mapOf(
            CommonArguments.SteamId to steamId
        ))

    fun friendsDestination(steamId: Long) =
        Routes.Friends.mapArgs(mapOf(
            CommonArguments.SteamId to steamId
        ))

    object Routes {
        const val NavGraph = "@profile"

        val Profile = DestNode("profile/{${CommonArguments.SteamId.name}}")
        val Friends = DestNode("profile/{${CommonArguments.SteamId.name}}/friends")
    }
}
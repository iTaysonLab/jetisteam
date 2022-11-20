package bruhcollective.itaysonlab.microapp.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.navigation.NavType
import androidx.navigation.navArgument
import bruhcollective.itaysonlab.microapp.core.*
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import bruhcollective.itaysonlab.microapp.profile.core.SectionType

abstract class ProfileMicroapp: NestedMicroappEntry, BottomNavigationCapable {
    override val graphRoute = Routes.NavGraph
    override val startDestination = Routes.Profile.url

    override val bottomNavigationEntry = NavigationEntry(
        route = Routes.NavGraph,
        name = R.string.profile,
        icon = { Icons.Rounded.Person }
    )

    fun myProfileDestination() = profileDestination(0L)

    fun profileDestination(steamId: Long) =
        Routes.Profile.mapArgs(mapOf(
            CommonArguments.SteamId to steamId
        ))

    fun friendsDestination(steamId: Long) =
        Routes.Friends.mapArgs(mapOf(
            CommonArguments.SteamId to steamId
        ))

    internal fun editDestination(steamId: Long) =
        Routes.Edit.mapArgs(mapOf(
            CommonArguments.SteamId to steamId
        ))

    internal fun editDestination(steamId: Long, section: SectionType) =
        Routes.EditSection.mapArgs(mapOf(
            CommonArguments.SteamId to steamId,
            Arguments.SectionType to section.name,
        ))

    object Arguments {
        val SectionType = navArgument("sectionType") {
            type = NavType.EnumType<SectionType>(SectionType::class.java)
        }
    }

    object Routes {
        const val NavGraph = "@profile"

        val Profile = DestNode("profile/{${CommonArguments.SteamId.name}}")
        val Friends = DestNode("profile/{${CommonArguments.SteamId.name}}/friends")

        val Edit = DestNode("profile/{${CommonArguments.SteamId.name}}/edit")
        val EditSection = DestNode("profile/{${CommonArguments.SteamId.name}}/edit/{${Arguments.SectionType.name}}")
    }
}
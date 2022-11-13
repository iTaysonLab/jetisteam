package bruhcollective.itaysonlab.microapp.library

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GridView
import androidx.navigation.NavType
import androidx.navigation.navArgument
import bruhcollective.itaysonlab.microapp.core.*
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments

abstract class LibraryMicroapp: NestedMicroappEntry, BottomNavigationCapable {
    override val graphRoute = InternalRoutes.NavGraph
    override val startDestination = InternalRoutes.Library.url

    override val bottomNavigationEntry = NavigationEntry(
        route = InternalRoutes.NavGraph,
        name = R.string.library,
        icon = { Icons.Rounded.GridView }
    )

    fun libraryOf(steamId: Long) = InternalRoutes.Library.mapArgs(mapOf(
        CommonArguments.SteamId to steamId
    ))

    internal object Arguments {
        val MachineId = navArgument("machineId") {
            type = NavType.LongType
        }

        val GameData = navArgument("gameData") {
            type = NavType.StringType
        }
    }

    internal object InternalRoutes {
        const val NavGraph = "@library"

        val Library = DestNode("library/{${CommonArguments.SteamId.name}}")
        val GameDetail = DestNode("library/{${CommonArguments.SteamId.name}}/games/{${Arguments.GameData.name}}")
        val RemoteMachineInfo = DestNode("library/{${CommonArguments.SteamId.name}}/machines/{${Arguments.MachineId.name}}")
    }
}
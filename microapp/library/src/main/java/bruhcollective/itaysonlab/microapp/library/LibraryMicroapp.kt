package bruhcollective.itaysonlab.microapp.library

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GridView
import androidx.navigation.NavType
import androidx.navigation.navArgument
import bruhcollective.itaysonlab.microapp.core.*
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments

abstract class LibraryMicroapp: NestedMicroappEntry, BottomNavigationCapable {
    override val graphRoute = Routes.NavGraph
    override val startDestination = Routes.Library.url

    override val bottomNavigationEntry = NavigationEntry(
        route = Routes.NavGraph,
        name = R.string.library,
        icon = { Icons.Rounded.GridView }
    )

    fun libraryOf(steamId: Long) = Routes.Library.mapArgs(mapOf(
        CommonArguments.SteamId to steamId
    ))

    internal object Arguments {
        val MachineId = navArgument("machineId") {
            type = NavType.LongType
        }

        val MachineList = navArgument("machinesList") {
            type = NavType.StringType
        }

        val GameData = navArgument("gameData") {
            type = NavType.StringType
        }

        val ApplicationId = navArgument("appId") {
            type = NavType.IntType
        }
    }

    internal object Routes {
        const val NavGraph = "@library"

        val Library = DestNode("library/{${CommonArguments.SteamId.name}}")
        val GameDetail = DestNode("library/{${CommonArguments.SteamId.name}}/games/{${Arguments.GameData.name}}")
        val RemoteMachineInfo = DestNode("library/{${CommonArguments.SteamId.name}}/machines/{${Arguments.MachineId.name}}")
        val PickRemoteMachine = DestNode("library/{${CommonArguments.SteamId.name}}/machines/list/{${Arguments.MachineList.name}}")
        val InstallGame = DestNode("library/{${CommonArguments.SteamId.name}}/games/{${Arguments.ApplicationId.name}}/install")
    }
}
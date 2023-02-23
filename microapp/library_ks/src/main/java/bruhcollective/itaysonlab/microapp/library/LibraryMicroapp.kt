package bruhcollective.itaysonlab.microapp.library

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.GridView
import androidx.compose.material.icons.twotone.GridView
import androidx.navigation.NavType
import androidx.navigation.navArgument
import bruhcollective.itaysonlab.microapp.core.BottomNavigationCapable
import bruhcollective.itaysonlab.microapp.core.DestNode
import bruhcollective.itaysonlab.microapp.core.NavigationEntry
import bruhcollective.itaysonlab.microapp.core.NestedMicroappEntry
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments

abstract class LibraryMicroapp: NestedMicroappEntry, BottomNavigationCapable {
    override val graphRoute = Routes.NavGraph
    override val startDestination = Routes.MyLibrary.url

    override val bottomNavigationEntry = NavigationEntry(
        route = Routes.NavGraph,
        name = R.string.library,
        icon = { Icons.TwoTone.GridView },
        iconSelected = { Icons.Sharp.GridView },
    )

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

        val MyLibrary = DestNode("library/")
        val GameDetail = DestNode("library/games/{${Arguments.GameData.name}}")

        val RemoteMachineInfo = DestNode("library/{${CommonArguments.SteamId.name}}/machines/{${Arguments.MachineId.name}}")
        val PickRemoteMachine = DestNode("library/{${CommonArguments.SteamId.name}}/machines/list/{${Arguments.MachineList.name}}")
        val InstallGame = DestNode("library/{${CommonArguments.SteamId.name}}/games/{${Arguments.ApplicationId.name}}/install")
    }
}
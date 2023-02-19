package bruhcollective.itaysonlab.microapp.library

import androidx.navigation.NavType
import androidx.navigation.navArgument
import bruhcollective.itaysonlab.microapp.core.BottomNavigationCapable
import bruhcollective.itaysonlab.microapp.core.DestNode
import bruhcollective.itaysonlab.microapp.core.NavigationEntry
import bruhcollective.itaysonlab.microapp.core.NestedMicroappEntry
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import solaricons.bold.SolarIconsBold
import solaricons.bold.solariconsbold.VideoAudioSound
import solaricons.bold.solariconsbold.videoaudiosound.Library
import solaricons.bold_duotone.SolarIconsBoldDuotone
import solaricons.bold_duotone.solariconsboldduotone.VideoAudioSound
import solaricons.bold_duotone.solariconsboldduotone.videoaudiosound.Library

abstract class LibraryMicroapp: NestedMicroappEntry, BottomNavigationCapable {
    override val graphRoute = Routes.NavGraph
    override val startDestination = Routes.MyLibrary.url

    override val bottomNavigationEntry = NavigationEntry(
        route = Routes.NavGraph,
        name = R.string.library,
        icon = { SolarIconsBoldDuotone.VideoAudioSound.Library },
        iconSelected = { SolarIconsBold.VideoAudioSound.Library },
    )

    /*fun libraryOf(steamId: Long) = Routes.Library.mapArgs(mapOf(
        CommonArguments.SteamId to steamId
    ))*/

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

        val MyLibrary = DestNode("library/me")

        val GameDetail = DestNode("library/{${CommonArguments.SteamId.name}}/games/{${Arguments.GameData.name}}")
        val RemoteMachineInfo = DestNode("library/{${CommonArguments.SteamId.name}}/machines/{${Arguments.MachineId.name}}")
        val PickRemoteMachine = DestNode("library/{${CommonArguments.SteamId.name}}/machines/list/{${Arguments.MachineList.name}}")
        val InstallGame = DestNode("library/{${CommonArguments.SteamId.name}}/games/{${Arguments.ApplicationId.name}}/install")
    }
}
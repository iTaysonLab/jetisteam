package bruhcollective.itaysonlab.microapp.guard

import androidx.navigation.NavType
import androidx.navigation.navArgument
import bruhcollective.itaysonlab.microapp.core.BottomNavigationCapable
import bruhcollective.itaysonlab.microapp.core.DestNode
import bruhcollective.itaysonlab.microapp.core.NestedMicroappEntry
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments

abstract class GuardMicroapp: NestedMicroappEntry, BottomNavigationCapable {
    override val graphRoute = Routes.NavGraph
    override val startDestination = Routes.MainScreen.url

    internal object Arguments {
        val ClientId = navArgument("clientId") {
            type = NavType.LongType
        }

        val GuardData = navArgument("guardData") {
            type = NavType.StringType
        }

        val SessionData = navArgument("sessionData") {
            type = NavType.StringType
        }

        val ConfirmationData = navArgument("confirmationData") {
            type = NavType.StringType
        }
    }

    internal object Routes {
        const val NavGraph = "@guard"

        val MainScreen = DestNode("guard")
        val Setup = DestNode("guard/{${CommonArguments.SteamId.name}}/setup/{${Arguments.GuardData.name}}")
        val Move = DestNode("guard/{${CommonArguments.SteamId.name}}/move")
        val Recovery = DestNode("guard/{${CommonArguments.SteamId.name}}/recovery")
        val Sessions = DestNode("guard/{${CommonArguments.SteamId.name}}/sessions")
        val SessionInfo = DestNode("guard/{${CommonArguments.SteamId.name}}/sessions/{${Arguments.SessionData.name}}")
        val MoreOptions = DestNode("guard/{${CommonArguments.SteamId.name}}/more")
        val ConfirmSignIn = DestNode("guard/{${CommonArguments.SteamId.name}}/confirm/{${Arguments.ClientId.name}}")
        val Remove = DestNode("guard/{${CommonArguments.SteamId.name}}/remove")
        val ScanQrCode = DestNode("guard/{${CommonArguments.SteamId.name}}/qrscan")
        val Confirmations = DestNode("guard/{${CommonArguments.SteamId.name}}/confirmations")
        val ConfirmationDetail = DestNode("guard/{${CommonArguments.SteamId.name}}/confirmation/{${Arguments.ConfirmationData.name}}")
    }
}
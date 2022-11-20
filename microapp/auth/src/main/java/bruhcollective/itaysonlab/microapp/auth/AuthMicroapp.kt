package bruhcollective.itaysonlab.microapp.auth

import androidx.navigation.NavType
import androidx.navigation.navArgument
import bruhcollective.itaysonlab.microapp.core.DestNode
import bruhcollective.itaysonlab.microapp.core.HasFullscreenRoutes
import bruhcollective.itaysonlab.microapp.core.NestedMicroappEntry

abstract class AuthMicroapp : NestedMicroappEntry, HasFullscreenRoutes {
    override val graphRoute = Routes.NavGraph
    override val startDestination = Routes.MainScreen.url

    override val fullscreenRoutes = listOf(
        Routes.MainScreen.url,
        Routes.AuthDisclaimer.url,
        Routes.TfaScreen.url
    )

    internal object Arguments {
        val MobileAuthEnabled = navArgument("hasRemoteConfirmation") {
            type = NavType.BoolType
        }
    }

    internal object Routes {
        const val NavGraph = "@auth"

        val MainScreen = DestNode("auth")
        val TfaScreen = DestNode("auth/tfa/{${Arguments.MobileAuthEnabled.name}}")
        val AuthDisclaimer = DestNode("dialogs/authDisclaimer")
    }
}
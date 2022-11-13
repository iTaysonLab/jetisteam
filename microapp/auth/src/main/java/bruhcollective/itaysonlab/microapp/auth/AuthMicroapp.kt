package bruhcollective.itaysonlab.microapp.auth

import androidx.navigation.NavType
import androidx.navigation.navArgument
import bruhcollective.itaysonlab.microapp.core.DestNode
import bruhcollective.itaysonlab.microapp.core.HasFullscreenRoutes
import bruhcollective.itaysonlab.microapp.core.NestedMicroappEntry

abstract class AuthMicroapp : NestedMicroappEntry, HasFullscreenRoutes {
    override val graphRoute = InternalRoutes.NavGraph
    override val startDestination = InternalRoutes.MainScreen.url

    override val fullscreenRoutes = listOf(
        InternalRoutes.MainScreen.url,
        InternalRoutes.AuthDisclaimer.url,
        InternalRoutes.TfaScreen.url
    )

    internal object Arguments {
        val MobileAuthEnabled = navArgument("hasRemoteConfirmation") {
            type = NavType.BoolType
        }
    }

    internal object InternalRoutes {
        const val NavGraph = "@auth"

        val MainScreen = DestNode("auth")
        val TfaScreen = DestNode("auth/tfa/{${Arguments.MobileAuthEnabled.name}}")
        val AuthDisclaimer = DestNode("dialogs/authDisclaimer")
    }
}
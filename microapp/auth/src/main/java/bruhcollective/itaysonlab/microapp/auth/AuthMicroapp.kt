package bruhcollective.itaysonlab.microapp.auth

import bruhcollective.itaysonlab.microapp.core.DestNode
import bruhcollective.itaysonlab.microapp.core.HasFullscreenRoutes
import bruhcollective.itaysonlab.microapp.core.NestedMicroappEntry

abstract class AuthMicroapp : NestedMicroappEntry, HasFullscreenRoutes {
    override val graphRoute = Routes.NavGraph
    override val startDestination = Routes.MainScreen.url

    override val fullscreenRoutes = listOf(
        Routes.MainScreen.url,
        Routes.SignInScreen.url,
        Routes.QrCodeScreen.url,
        Routes.TfaScreen.url
    )

    internal object Arguments {

    }

    internal object Routes {
        const val NavGraph = "@auth"

        val MainScreen = DestNode("auth/onboarding")

        val SignInScreen = DestNode("auth/username")
        val QrCodeScreen = DestNode("auth/qrcode")

        val TfaScreen = DestNode("auth/tfa/")
    }
}
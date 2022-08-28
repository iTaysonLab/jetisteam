package bruhcollective.itaysonlab.microapp.auth

import bruhcollective.itaysonlab.microapp.core.NestedMicroappEntry

abstract class AuthMicroapp: NestedMicroappEntry {
    override val microappRoute = "auth"
    override val fullscreenRoutes = listOf("auth", AuthMicroappImpl.InternalRoutes.AuthDisclaimer, AuthMicroappImpl.InternalRoutes.TfaScreen)
}
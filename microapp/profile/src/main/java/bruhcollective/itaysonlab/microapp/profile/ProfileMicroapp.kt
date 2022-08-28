package bruhcollective.itaysonlab.microapp.profile

import bruhcollective.itaysonlab.microapp.core.ComposableMicroappEntry

abstract class ProfileMicroapp: ComposableMicroappEntry {
    override val microappRoute = "profile"
}
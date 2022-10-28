package bruhcollective.itaysonlab.microapp.profile

import bruhcollective.itaysonlab.microapp.core.BottomNavigationCapable
import bruhcollective.itaysonlab.microapp.core.NestedMicroappEntry

abstract class ProfileMicroapp: NestedMicroappEntry, BottomNavigationCapable {
    override val microappRoute = "profile"
}
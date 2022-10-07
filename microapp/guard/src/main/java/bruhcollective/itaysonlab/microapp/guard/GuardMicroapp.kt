package bruhcollective.itaysonlab.microapp.guard

import bruhcollective.itaysonlab.microapp.core.BottomNavigationCapable
import bruhcollective.itaysonlab.microapp.core.NestedMicroappEntry

abstract class GuardMicroapp: NestedMicroappEntry, BottomNavigationCapable {
    override val microappRoute = "guard"
}
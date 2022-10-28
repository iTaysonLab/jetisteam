package bruhcollective.itaysonlab.microapp.library

import bruhcollective.itaysonlab.microapp.core.BottomNavigationCapable
import bruhcollective.itaysonlab.microapp.core.NestedMicroappEntry
import bruhcollective.itaysonlab.microapp.core.map

abstract class LibraryMicroapp: NestedMicroappEntry, BottomNavigationCapable {
    override val microappRoute = "library"

    companion object {
        fun libraryOf(steamId: Long) = LibraryMicroappImpl.InternalRoutes.Library.map(mapOf(
            LibraryMicroappImpl.InternalRoutes.ARG_STEAM_ID to steamId.toString()
        ))
    }
}
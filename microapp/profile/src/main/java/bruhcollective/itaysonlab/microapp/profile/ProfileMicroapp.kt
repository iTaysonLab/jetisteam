package bruhcollective.itaysonlab.microapp.profile

import bruhcollective.itaysonlab.microapp.core.BottomNavigationCapable
import bruhcollective.itaysonlab.microapp.core.NestedMicroappEntry
import bruhcollective.itaysonlab.microapp.profile.ProfileMicroappImpl.InternalRoutes.ARG_ID

abstract class ProfileMicroapp: NestedMicroappEntry, BottomNavigationCapable {
    override val microappRoute = "profile"
    fun libraryDestination(steamId: Long) = ProfileMicroappImpl.InternalRoutes.Library.replace("{$ARG_ID}", steamId.toString())
}
package bruhcollective.itaysonlab.microapp.notifications

import bruhcollective.itaysonlab.microapp.core.BottomNavigationCapable
import bruhcollective.itaysonlab.microapp.core.ComposableMicroappEntry

abstract class NotificationsMicroapp: ComposableMicroappEntry, BottomNavigationCapable {
    override val microappRoute = "notifications"
}
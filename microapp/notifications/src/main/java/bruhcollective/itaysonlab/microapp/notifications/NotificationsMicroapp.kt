package bruhcollective.itaysonlab.microapp.notifications

import bruhcollective.itaysonlab.microapp.core.ComposableMicroappEntry

abstract class NotificationsMicroapp: ComposableMicroappEntry {
    override val microappRoute = "notifications"
}
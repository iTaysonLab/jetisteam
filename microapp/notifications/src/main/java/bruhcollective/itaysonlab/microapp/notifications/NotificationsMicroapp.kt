package bruhcollective.itaysonlab.microapp.notifications

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import bruhcollective.itaysonlab.microapp.core.BottomNavigationCapable
import bruhcollective.itaysonlab.microapp.core.ComposableMicroappEntry
import bruhcollective.itaysonlab.microapp.core.NavigationEntry

abstract class NotificationsMicroapp: ComposableMicroappEntry, BottomNavigationCapable {
    override val microappRoute = InternalRoutes.MainScreen

    override val bottomNavigationEntry = NavigationEntry(
        route = InternalRoutes.MainScreen,
        name = R.string.notifications,
        icon = { Icons.Rounded.Notifications }
    )

    internal object InternalRoutes {
        const val MainScreen = "notifications"
    }
}
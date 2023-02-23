package bruhcollective.itaysonlab.microapp.notifications

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.twotone.Notifications
import bruhcollective.itaysonlab.microapp.core.BottomNavigationCapable
import bruhcollective.itaysonlab.microapp.core.ComposableMicroappEntry
import bruhcollective.itaysonlab.microapp.core.NavigationEntry

abstract class NotificationsMicroapp: ComposableMicroappEntry, BottomNavigationCapable {
    override val microappRoute = Routes.MainScreen

    override val bottomNavigationEntry = NavigationEntry(
        route = Routes.MainScreen,
        name = R.string.notifications,
        icon = { Icons.TwoTone.Notifications },
        iconSelected = { Icons.Filled.Notifications },
    )

    internal object Routes {
        const val MainScreen = "notifications"
    }
}
package bruhcollective.itaysonlab.microapp.notifications

import bruhcollective.itaysonlab.microapp.core.BottomNavigationCapable
import bruhcollective.itaysonlab.microapp.core.ComposableMicroappEntry
import bruhcollective.itaysonlab.microapp.core.NavigationEntry
import solaricons.bold.SolarIconsBold
import solaricons.bold.solariconsbold.Notifications
import solaricons.bold.solariconsbold.notifications.Bell
import solaricons.bold_duotone.SolarIconsBoldDuotone
import solaricons.bold_duotone.solariconsboldduotone.Notifications
import solaricons.bold_duotone.solariconsboldduotone.notifications.Bell

abstract class NotificationsMicroapp: ComposableMicroappEntry, BottomNavigationCapable {
    override val microappRoute = Routes.MainScreen

    override val bottomNavigationEntry = NavigationEntry(
        route = Routes.MainScreen,
        name = R.string.notifications,
        icon = { SolarIconsBoldDuotone.Notifications.Bell },
        iconSelected = { SolarIconsBold.Notifications.Bell },
    )

    internal object Routes {
        const val MainScreen = "notifications"
    }
}
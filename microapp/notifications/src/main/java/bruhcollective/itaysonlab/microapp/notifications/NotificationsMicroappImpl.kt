package bruhcollective.itaysonlab.microapp.notifications

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.NavigationEntry
import bruhcollective.itaysonlab.microapp.notifications.ui.NotificationsScreen
import javax.inject.Inject

class NotificationsMicroappImpl @Inject constructor() : NotificationsMicroapp() {
    @Composable
    override fun NavGraphBuilder.Content(
        navController: NavHostController,
        destinations: Destinations,
        backStackEntry: NavBackStackEntry
    ) {
        NotificationsScreen()
    }

    override val bottomNavigationEntry = NavigationEntry(
        route = microappRoute,
        name = R.string.notifications,
        icon = { Icons.Rounded.Notifications }
    )
}
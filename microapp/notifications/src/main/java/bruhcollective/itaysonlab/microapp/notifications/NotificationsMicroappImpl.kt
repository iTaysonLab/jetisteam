package bruhcollective.itaysonlab.microapp.notifications

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalUriHandler
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.notifications.ui.NotificationsScreen
import javax.inject.Inject

class NotificationsMicroappImpl @Inject constructor() : NotificationsMicroapp() {
    @Composable
    override fun NavGraphBuilder.Content(
        navController: NavHostController,
        destinations: Destinations,
        backStackEntry: NavBackStackEntry
    ) {
        val urlHandler = LocalUriHandler.current

        NotificationsScreen(onClick = { notification ->
            /*when (notification.type) {
                SteamNotificationType.Wishlist -> navController.navigate(destinations.find<GamePageMicroapp>().gameDestination(notification.destination as Int))
                SteamNotificationType.FriendInvite -> navController.navigate(destinations.find<ProfileMicroapp>().profileDestination(notification.destination as Long))
                SteamNotificationType.Promotion -> urlHandler.openUri(notification.destination as String)
                else -> {}
            }*/
        })
    }
}
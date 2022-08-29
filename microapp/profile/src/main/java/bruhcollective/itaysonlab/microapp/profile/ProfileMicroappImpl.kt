package bruhcollective.itaysonlab.microapp.profile

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.find
import bruhcollective.itaysonlab.microapp.gamepage.GamePageMicroapp
import bruhcollective.itaysonlab.microapp.profile.ui.ProfileScreen
import javax.inject.Inject

class ProfileMicroappImpl @Inject constructor(): ProfileMicroapp() {
    @Composable
    override fun NavGraphBuilder.Content(
        navController: NavHostController,
        destinations: Destinations,
        backStackEntry: NavBackStackEntry
    ) {
        ProfileScreen(
            onGameClick = { appId ->
                navController.navigate(
                    destinations.find<GamePageMicroapp>().gameDestination(appId)
                )
            }
        )
    }
}
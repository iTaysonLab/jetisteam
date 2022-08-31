package bruhcollective.itaysonlab.microapp.profile

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.find
import bruhcollective.itaysonlab.microapp.gamepage.GamePageMicroapp
import bruhcollective.itaysonlab.microapp.profile.ui.ProfileScreen
import bruhcollective.itaysonlab.microapp.profile.ui.screens.library.LibraryScreen
import javax.inject.Inject

class ProfileMicroappImpl @Inject constructor(): ProfileMicroapp() {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        destinations: Destinations
    ) {
        navigation(startDestination = microappRoute, route = InternalRoutes.NavGraph) {
            composable(microappRoute) {
                ProfileScreen(
                    onGameClick = { appId ->
                        navController.navigateToGame(destinations, appId)
                    }, onLibraryClick = { steamId ->
                        navController.navigate(
                            libraryDestination(steamId)
                        )
                    }
                )
            }

            composable(InternalRoutes.Library) {
                LibraryScreen(
                    onGameClick = { appId ->
                        navController.navigateToGame(destinations, appId)
                    }, onBackClick = navController::popBackStack
                )
            }
        }
    }

    private fun NavController.navigateToGame(destinations: Destinations, appId: Int) {
        navigate(
            destinations.find<GamePageMicroapp>().gameDestination(appId)
        )
    }

    object InternalRoutes {
        const val NavGraph = "@auth"

        const val ARG_ID = "id"

        const val Library = "profile/{$ARG_ID}/library"
    }
}
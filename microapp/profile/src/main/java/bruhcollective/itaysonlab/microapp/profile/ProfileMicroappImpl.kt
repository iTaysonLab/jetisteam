package bruhcollective.itaysonlab.microapp.profile

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.NavigationEntry
import bruhcollective.itaysonlab.microapp.core.find
import bruhcollective.itaysonlab.microapp.gamepage.GamePageMicroapp
import bruhcollective.itaysonlab.microapp.library.LibraryMicroapp
import bruhcollective.itaysonlab.microapp.profile.ui.ProfileScreen
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
                        navController.navigate(LibraryMicroapp.libraryOf(steamId))
                    }
                )
            }
        }
    }

    private fun NavController.navigateToGame(destinations: Destinations, appId: Int) {
        navigate(GamePageMicroapp.gameDestination(appId))
    }

    override val bottomNavigationEntry = NavigationEntry(
        route = InternalRoutes.NavGraph,
        name = R.string.profile,
        icon = { Icons.Rounded.Person }
    )

    object InternalRoutes {
        const val NavGraph = "@profile"

        const val ARG_ID = "id"
    }
}
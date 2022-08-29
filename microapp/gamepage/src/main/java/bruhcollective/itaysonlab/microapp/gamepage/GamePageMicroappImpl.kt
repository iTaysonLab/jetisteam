package bruhcollective.itaysonlab.microapp.gamepage

import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.gamepage.ui.GamePageScreen
import javax.inject.Inject

class GamePageMicroappImpl @Inject constructor() : GamePageMicroapp() {
    @Composable
    override fun NavGraphBuilder.Content(
        navController: NavHostController,
        destinations: Destinations,
        backStackEntry: NavBackStackEntry
    ) {
        GamePageScreen()
    }
}
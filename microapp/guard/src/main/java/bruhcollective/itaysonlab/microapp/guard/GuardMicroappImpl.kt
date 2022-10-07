package bruhcollective.itaysonlab.microapp.guard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Security
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.NavigationEntry
import javax.inject.Inject

class GuardMicroappImpl @Inject constructor(): GuardMicroapp() {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        destinations: Destinations
    ) {
        navigation(startDestination = microappRoute, route = InternalRoutes.NavGraph) {
            composable(microappRoute) {

            }
        }
    }

    override val bottomNavigationEntry = NavigationEntry(
        route = microappRoute,
        name = R.string.guard,
        icon = { Icons.Rounded.Security }
    )

    object InternalRoutes {
        const val NavGraph = "@guard"

        const val ARG_STEAM_ID = "steamid"

        const val Setup = "guard/{$ARG_STEAM_ID}/setup"
    }
}
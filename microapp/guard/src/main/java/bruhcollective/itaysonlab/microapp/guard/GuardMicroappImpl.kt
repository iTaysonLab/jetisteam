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
import bruhcollective.itaysonlab.microapp.core.ext.ROOT_NAV_GRAPH_ID
import bruhcollective.itaysonlab.microapp.guard.ui.GuardScreen
import bruhcollective.itaysonlab.microapp.guard.ui.recovery.GuardRecoveryCodeScreen
import bruhcollective.itaysonlab.microapp.guard.ui.setup.GuardSetupScreen
import javax.inject.Inject

class GuardMicroappImpl @Inject constructor(): GuardMicroapp() {
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        destinations: Destinations
    ) {
        navigation(startDestination = microappRoute, route = InternalRoutes.NavGraph) {
            composable(microappRoute) {
                GuardScreen(dbg = {
                    navController.navigate("guard/0/setup")
                })
            }

            composable(InternalRoutes.Setup) {
                GuardSetupScreen(onBackClicked = navController::popBackStack, dbg = {
                    navController.navigate("guard/0/recovery")
                })
            }

            composable(InternalRoutes.Recovery) {
                GuardRecoveryCodeScreen(onBackClicked = {
                    navController.navigate(microappRoute) {
                        popUpTo(ROOT_NAV_GRAPH_ID)
                    }
                })
            }
        }
    }

    override val bottomNavigationEntry = NavigationEntry(
        route = microappRoute,
        name = R.string.guard,
        icon = { Icons.Rounded.Security }
    )

    internal object InternalRoutes {
        const val NavGraph = "@guard"

        const val ARG_STEAM_ID = "steamid"

        const val Setup = "guard/{$ARG_STEAM_ID}/setup"
        const val Recovery = "guard/{$ARG_STEAM_ID}/recovery"
    }
}
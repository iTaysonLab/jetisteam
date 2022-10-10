package bruhcollective.itaysonlab.microapp.guard

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Security
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import bruhcollective.itaysonlab.microapp.core.DestNode
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.NavigationEntry
import bruhcollective.itaysonlab.microapp.core.ext.ROOT_NAV_GRAPH_ID
import bruhcollective.itaysonlab.microapp.core.map
import bruhcollective.itaysonlab.microapp.guard.ui.GuardScreen
import bruhcollective.itaysonlab.microapp.guard.ui.bottomsheet.GuardMoreOptionsSheet
import bruhcollective.itaysonlab.microapp.guard.ui.devices.GuardDevicesScreen
import bruhcollective.itaysonlab.microapp.guard.ui.recovery.GuardRecoveryCodeScreen
import bruhcollective.itaysonlab.microapp.guard.ui.setup.GuardSetupScreen
import bruhcollective.itaysonlab.microapp.guard.ui.setup.variants.GuardMoveScreen
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import javax.inject.Inject

class GuardMicroappImpl @Inject constructor(): GuardMicroapp() {
    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        destinations: Destinations
    ) {
        navigation(startDestination = microappRoute, route = InternalRoutes.NavGraph) {
            composable(microappRoute) {
                GuardScreen(onMoveClicked = { steamId ->
                    navController.navigate(InternalRoutes.Move.withSteamId(steamId))
                }, onAddClicked = { steamId ->
                    navController.navigate(InternalRoutes.Setup.withSteamId(steamId))
                }, onMoreClicked = { steamId ->
                    navController.navigate(InternalRoutes.MoreOptions.withSteamId(steamId))
                })
            }

            composable(InternalRoutes.Setup.url) {
                /*GuardSetupScreen(onBackClicked = navController::popBackStack, dbg = {
                    navController.navigate("guard/0/recovery")
                })*/
            }

            composable(InternalRoutes.Move.url) {
                GuardMoveScreen(onBackClicked = navController::popBackStack, onSuccess = { steamId ->
                    navController.navigate(InternalRoutes.Recovery.withSteamId(steamId.steamId))
                })
            }

            composable(InternalRoutes.Recovery.url) {
                GuardRecoveryCodeScreen(onBackClicked = {
                    navController.navigate(microappRoute) {
                        popUpTo(ROOT_NAV_GRAPH_ID)
                    }
                })
            }

            composable(InternalRoutes.Sessions.url) {
                GuardDevicesScreen(onBackClicked = navController::popBackStack)
            }

            bottomSheet(InternalRoutes.MoreOptions.url) {
                GuardMoreOptionsSheet(onDevicesClicked = { steamId ->
                    navController.popBackStack()
                    navController.navigate(InternalRoutes.Sessions.withSteamId(steamId))
                }, onRemoveClicked = { steamId ->
                    navController.popBackStack()
                    // TODO
                }, onRecoveryClicked = { steamId ->
                    navController.popBackStack()
                    navController.navigate(InternalRoutes.Recovery.withSteamId(steamId))
                })
            }
        }
    }

    private fun DestNode.withSteamId(steamId: Long) = map(mapOf(
        InternalRoutes.ARG_STEAM_ID to steamId.toString()
    ))

    override val bottomNavigationEntry = NavigationEntry(
        route = microappRoute,
        name = R.string.guard,
        icon = { Icons.Rounded.Security }
    )

    internal object InternalRoutes {
        const val NavGraph = "@guard"

        const val ARG_STEAM_ID = "steamid"

        val Setup = DestNode("guard/{$ARG_STEAM_ID}/setup")
        val Move = DestNode("guard/{$ARG_STEAM_ID}/move")
        val Recovery = DestNode("guard/{$ARG_STEAM_ID}/recovery")
        val Sessions = DestNode("guard/{$ARG_STEAM_ID}/sessions")
        val MoreOptions = DestNode("guard/{$ARG_STEAM_ID}/more")
    }
}
package bruhcollective.itaysonlab.microapp.library

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.GridView
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import bruhcollective.itaysonlab.microapp.core.DestNode
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.NavigationEntry
import bruhcollective.itaysonlab.microapp.core.map
import bruhcollective.itaysonlab.microapp.gamepage.GamePageMicroapp
import bruhcollective.itaysonlab.microapp.library.ui.LibraryScreen
import bruhcollective.itaysonlab.microapp.library.ui.bottomsheet.OwnedGameBottomSheet
import bruhcollective.itaysonlab.microapp.library.ui.remote.RemoteMachineScreen
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import javax.inject.Inject

class LibraryMicroappImpl @Inject constructor(): LibraryMicroapp() {
    @OptIn(ExperimentalMaterialNavigationApi::class)
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        destinations: Destinations
    ) {
        navigation(startDestination = microappRoute, route = InternalRoutes.NavGraph) {
            composable(microappRoute) {

            }

            composable(InternalRoutes.Library.url) {
                LibraryScreen(onGameClick = { steamId, gameInfo ->
                    navController.navigate(InternalRoutes.GameDetail.map(mapOf(
                        InternalRoutes.ARG_STEAM_ID to steamId.toString(),
                        InternalRoutes.ARG_GAME_INFO to gameInfo,
                    )))
                }, onBackClick = navController::popBackStack, onRemoteClick = { steamId, machineId ->
                    navController.navigate(InternalRoutes.RemoteMachineInfo.map(mapOf(
                        InternalRoutes.ARG_STEAM_ID to steamId.toString(),
                        InternalRoutes.ARG_MACHINE_ID to machineId.toString(),
                    )))
                })
            }

            composable(InternalRoutes.RemoteMachineInfo.url) {
               RemoteMachineScreen(onBackClick = navController::popBackStack)
            }

            bottomSheet(InternalRoutes.GameDetail.url) {
                OwnedGameBottomSheet(onNavigateToAchievements = { steamId, appId ->
                    navController.popBackStack()
                }, onNavigateToGamePage = { appId ->
                    navController.popBackStack()
                    navController.navigate(GamePageMicroapp.gameDestination(appId))
                }, onOpenRemoteInstallations = { steamId, appId ->
                    navController.popBackStack()
                })
            }
        }
    }

    override val bottomNavigationEntry = NavigationEntry(
        route = InternalRoutes.NavGraph,
        name = R.string.library,
        icon = { Icons.Rounded.GridView }
    )

    internal object InternalRoutes {
        const val NavGraph = "@library"

        const val ARG_STEAM_ID = "steamId"
        const val ARG_MACHINE_ID = "machineId"

        const val ARG_GAME_INFO = "gameData"

        val Library = DestNode("library/{$ARG_STEAM_ID}")
        val GameDetail = DestNode("library/{$ARG_STEAM_ID}/games/{${ARG_GAME_INFO}}")

        val RemoteMachineInfo = DestNode("library/{$ARG_STEAM_ID}/machines/{${ARG_MACHINE_ID}}")
    }
}
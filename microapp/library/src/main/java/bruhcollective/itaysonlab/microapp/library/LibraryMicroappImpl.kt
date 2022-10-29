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
                }, onBackClick = navController::popBackStack)
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
        route = microappRoute,
        name = R.string.library,
        icon = { Icons.Rounded.GridView }
    )

    internal object InternalRoutes {
        const val NavGraph = "@library"

        const val ARG_STEAM_ID = "steamId"
        const val ARG_GAME_INFO = "gameData"
        const val ARG_MACHINE_INFO = "machineData"

        val Library = DestNode("library/{$ARG_STEAM_ID}")
        val GameDetail = DestNode("library/{$ARG_STEAM_ID}/games/{${ARG_GAME_INFO}}")
    }
}
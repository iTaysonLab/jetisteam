package bruhcollective.itaysonlab.microapp.library

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.find
import bruhcollective.itaysonlab.microapp.core.mapArgs
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import bruhcollective.itaysonlab.microapp.gamepage.GamePageMicroapp
import bruhcollective.itaysonlab.microapp.library.ui.LibraryScreen
import bruhcollective.itaysonlab.microapp.library.ui.bottomsheet.OwnedGameBottomSheet
import bruhcollective.itaysonlab.microapp.library.ui.remote.RemoteMachineScreen
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import javax.inject.Inject

class LibraryMicroappImpl @Inject constructor(): LibraryMicroapp() {
    @OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
    override fun NavGraphBuilder.buildGraph(
        navController: NavHostController,
        destinations: Destinations
    ) {
        composable(Routes.Library.url, arguments = listOf(CommonArguments.SteamId)) {
            LibraryScreen(onGameClick = { steamId, gameInfo ->
                navController.navigate(Routes.GameDetail.mapArgs(mapOf(
                    CommonArguments.SteamId to steamId,
                    Arguments.GameData to gameInfo,
                )))
            }, onBackClick = navController::popBackStack, onRemoteClick = { steamId, machineId ->
                navController.navigate(Routes.RemoteMachineInfo.mapArgs(mapOf(
                    CommonArguments.SteamId to steamId,
                    Arguments.MachineId to machineId,
                )))
            })
        }

        composable(Routes.RemoteMachineInfo.url, arguments = listOf(CommonArguments.SteamId, Arguments.MachineId)) {
            RemoteMachineScreen(onBackClick = navController::popBackStack)
        }

        bottomSheet(Routes.GameDetail.url, arguments = listOf(CommonArguments.SteamId, Arguments.GameData)) {
            OwnedGameBottomSheet(onNavigateToAchievements = { steamId, appId ->
                navController.popBackStack()
            }, onNavigateToGamePage = { appId ->
                navController.popBackStack()
                navController.navigate(destinations.find<GamePageMicroapp>().gameDestination(appId))
            }, onOpenRemoteInstallations = { steamId, appId ->
                navController.popBackStack()
            })
        }
    }
}
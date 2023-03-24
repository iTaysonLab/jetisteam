package bruhcollective.itaysonlab.microapp.library

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.mapArgs
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import bruhcollective.itaysonlab.microapp.library.ui.LibraryScreen
import bruhcollective.itaysonlab.microapp.library.ui.game_page.LibraryGamepageScreen
import com.google.accompanist.navigation.animation.composable
import javax.inject.Inject

class LibraryMicroappImpl @Inject constructor(): LibraryMicroapp() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun NavGraphBuilder.buildGraph(
        navController: NavHostController,
        destinations: Destinations
    ) {
        composable(Routes.MyLibrary.url, arguments = listOf(CommonArguments.SteamIdWithDefault)) {
            LibraryScreen(onApplicationClick = { appId ->
                navController.navigate(Routes.GameDetail.mapArgs(mapOf(
                    Arguments.ApplicationId to appId
                )))
            }, /*onRemoteClick = { steamId, sessions ->
                if (sessions.size > 1) {
                    navController.navigate(Routes.PickRemoteMachine.mapArgs(mapOf(
                        CommonArguments.SteamId to steamId,
                        Arguments.MachineList to sessions.joinToString("-") { it.asBase64() }
                    )))
                } else {
                    navController.navigate(Routes.RemoteMachineInfo.mapArgs(mapOf(
                        CommonArguments.SteamId to steamId,
                        Arguments.MachineId to (sessions.firstOrNull()?.client_instanceid ?: return@LibraryScreen)
                    )))
                }
            }*/)
        }

        /*composable(Routes.RemoteMachineInfo.url, arguments = listOf(CommonArguments.SteamId, Arguments.MachineId)) {
            RemoteMachineScreen(onBackClick = navController::popBackStack, onVisitStoreClicked = { appId ->
                navController.navigate(destinations.find<GamePageMicroapp>().gameDestination(appId))
            })
        }*/

        composable(Routes.GameDetail.url, arguments = listOf(Arguments.ApplicationId)) {
            LibraryGamepageScreen()
        }

        /*bottomSheet(Routes.PickRemoteMachine.url, arguments = listOf(CommonArguments.SteamId, Arguments.MachineList)) {
            PickRemoteDeviceBottomSheet(onMachinePicked = { steamId, machineId ->
                navController.popBackStack()
                navController.navigate(Routes.RemoteMachineInfo.mapArgs(mapOf(
                    CommonArguments.SteamId to steamId,
                    Arguments.MachineId to machineId,
                )))
            })
        }

        bottomSheet(Routes.InstallGame.url, arguments = listOf(CommonArguments.SteamId, Arguments.ApplicationId)) {
            InstallGameBottomSheet(onSuccess = navController::popBackStack, onOpenMachine = { steamId, machineId ->
                navController.popBackStack()
                navController.navigate(Routes.RemoteMachineInfo.mapArgs(mapOf(
                    CommonArguments.SteamId to steamId,
                    Arguments.MachineId to machineId
                )))
            })
        }*/
    }
}
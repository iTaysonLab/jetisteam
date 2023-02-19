package bruhcollective.itaysonlab.microapp.guard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import bruhcollective.itaysonlab.microapp.core.DestNode
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.NavigationEntry
import bruhcollective.itaysonlab.microapp.core.ext.ROOT_NAV_GRAPH_ID
import bruhcollective.itaysonlab.microapp.core.mapArgs
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.results.setResultToPreviousEntry
import bruhcollective.itaysonlab.microapp.guard.ui.GuardScreen
import bruhcollective.itaysonlab.microapp.guard.ui.bottomsheet.GuardConfirmSessionSheet
import bruhcollective.itaysonlab.microapp.guard.ui.bottomsheet.GuardRecoveryCodeSheet
import bruhcollective.itaysonlab.microapp.guard.ui.bottomsheet.GuardRemoveSheet
import bruhcollective.itaysonlab.microapp.guard.ui.confirmation_detail.ConfirmationDetailScreen
import bruhcollective.itaysonlab.microapp.guard.ui.qrsign.QrSignScreen
import bruhcollective.itaysonlab.microapp.guard.ui.session_detail.GuardSessionScreen
import bruhcollective.itaysonlab.microapp.guard.ui.setup.GuardSetupScreen
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import solaricons.bold.SolarIconsBold
import solaricons.bold.solariconsbold.Security
import solaricons.bold.solariconsbold.security.Shield
import solaricons.bold_duotone.SolarIconsBoldDuotone
import solaricons.bold_duotone.solariconsboldduotone.Security
import solaricons.bold_duotone.solariconsboldduotone.security.Shield
import soup.compose.material.motion.animation.materialSharedAxisYIn
import soup.compose.material.motion.animation.materialSharedAxisYOut
import javax.inject.Inject

class GuardMicroappImpl @Inject constructor(): GuardMicroapp() {
    @OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
    override fun NavGraphBuilder.buildGraph(
        navController: NavHostController,
        destinations: Destinations
    ) {
        composable(Routes.MainScreen.url) {
            GuardScreen(onAddClicked = { steamId ->
                navController.navigate(Routes.Setup.withSteamId(steamId))
            }, onDeleteClicked = { steamId ->
                navController.navigate(Routes.Remove.withSteamId(steamId))
            }, onQrClicked = { steamId ->
                navController.navigate(Routes.ScanQrCode.withSteamId(steamId))
            }, onRecoveryClicked = { steamId ->
                navController.navigate(Routes.Recovery.withSteamId(steamId))
            }, onConfirmationClicked = { steamId, data ->
                navController.navigate(Routes.ConfirmationDetail.mapArgs(mapOf(
                    CommonArguments.SteamId to steamId,
                    Arguments.ConfirmationData to data
                )))
            }, onSessionClicked = { steamId, session ->
                navController.navigate(Routes.SessionInfo.mapArgs(mapOf(
                    CommonArguments.SteamId to steamId,
                    Arguments.SessionData to session.protoBytes().base64Url()
                )))
            }, onSessionArrived = { steamId, clientId ->
                navController.navigate(Routes.ConfirmSignIn.mapArgs(mapOf(
                    CommonArguments.SteamId to steamId,
                    Arguments.ClientId to clientId,
                )))
            }, backStack = it)
        }

        composable(Routes.Setup.url, arguments = listOf(CommonArguments.SteamId)) {
            GuardSetupScreen(onBackClicked = navController::popBackStack, onSuccess = { steamId ->
                navController.navigateToRoot()
            })
        }

        composable(Routes.ConfirmationDetail.url, arguments = listOf(CommonArguments.SteamId, Arguments.ConfirmationData)) {
            ConfirmationDetailScreen(onBackClicked = navController::popBackStack, onFinish = { event ->
                navController.setResultToPreviousEntry(event)
            })
        }

        composable(Routes.SessionInfo.url, arguments = listOf(CommonArguments.SteamId, Arguments.SessionData)) {
            GuardSessionScreen(onBackClicked = navController::popBackStack)
        }

        composable(Routes.ScanQrCode.url, arguments = listOf(CommonArguments.SteamId), enterTransition = {
            materialSharedAxisYIn(forward = true, slideDistance = 300)
        }, exitTransition = {
            materialSharedAxisYOut(forward = true, slideDistance = 300)
        }, popEnterTransition = {
            materialSharedAxisYIn(forward = false, slideDistance = 300)
        }, popExitTransition = {
            materialSharedAxisYOut(forward = false, slideDistance = 300)
        }) {
            QrSignScreen(onBackClicked = navController::popBackStack, onFinish = { event ->
                navController.setResultToPreviousEntry(event)
            })
        }

        bottomSheet(Routes.ConfirmSignIn.url, arguments = listOf(
            CommonArguments.SteamId,
            Arguments.ClientId
        )) {
            GuardConfirmSessionSheet(onFinish = { event ->
                navController.setResultToPreviousEntry(event)
            })
        }

        bottomSheet(Routes.Recovery.url, arguments = listOf(
            CommonArguments.SteamId
        )) {
            GuardRecoveryCodeSheet(onExit = navController::popBackStack)
        }

        bottomSheet(Routes.Remove.url, arguments = listOf(CommonArguments.SteamId)) {
            GuardRemoveSheet(onGuardRemoved = {
                navController.popBackStack()
                navController.navigateToRoot()
            }, onGuardRemovalCancelled = navController::popBackStack)
        }
    }

    private fun DestNode.withSteamId(steamId: Long) = mapArgs(mapOf(
        CommonArguments.SteamId to steamId
    ))

    private fun NavHostController.navigateToRoot() {
        navigate(Routes.MainScreen.url) {
            popUpTo(ROOT_NAV_GRAPH_ID)
        }
    }

    override val bottomNavigationEntry = NavigationEntry(
        route = Routes.NavGraph,
        name = R.string.guard,
        icon = { SolarIconsBoldDuotone.Security.Shield },
        iconSelected = { SolarIconsBold.Security.Shield },
    )
}
package bruhcollective.itaysonlab.microapp.guard

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Security
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
import bruhcollective.itaysonlab.microapp.guard.ui.bottomsheet.GuardMoreOptionsSheet
import bruhcollective.itaysonlab.microapp.guard.ui.bottomsheet.GuardRemoveSheet
import bruhcollective.itaysonlab.microapp.guard.ui.confirmation_detail.ConfirmationDetailScreen
import bruhcollective.itaysonlab.microapp.guard.ui.confirmations.ConfirmationsScreen
import bruhcollective.itaysonlab.microapp.guard.ui.devices.GuardDevicesScreen
import bruhcollective.itaysonlab.microapp.guard.ui.devices.session.GuardSessionScreen
import bruhcollective.itaysonlab.microapp.guard.ui.qrsign.QrSignScreen
import bruhcollective.itaysonlab.microapp.guard.ui.recovery.GuardRecoveryCodeScreen
import bruhcollective.itaysonlab.microapp.guard.ui.setup.variants.GuardMoveScreen
import bruhcollective.itaysonlab.microapp.guard.ui.setup.variants.GuardSetupScreen
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import soup.compose.material.motion.animation.materialSharedAxisYIn
import soup.compose.material.motion.animation.materialSharedAxisYOut
import steam.auth.CAuthentication_RefreshToken_Enumerate_Response
import steam.twofactor.CTwoFactor_AddAuthenticator_Response
import javax.inject.Inject

class GuardMicroappImpl @Inject constructor(): GuardMicroapp() {
    @OptIn(ExperimentalMaterialNavigationApi::class, ExperimentalAnimationApi::class)
    override fun NavGraphBuilder.buildGraph(
        navController: NavHostController,
        destinations: Destinations
    ) {
        composable(Routes.MainScreen.url) {
            GuardScreen(onMoveClicked = { steamId ->
                navController.navigate(Routes.Move.withSteamId(steamId))
            }, onAddClicked = { steamId, data ->
                navController.navigate(Routes.Setup.mapArgs(mapOf(
                    CommonArguments.SteamId to steamId,
                    Arguments.GuardData to CTwoFactor_AddAuthenticator_Response.ADAPTER.encodeByteString(
                        data
                    ).base64Url()
                )))
            }, onMoreClicked = { steamId ->
                navController.navigate(Routes.MoreOptions.withSteamId(steamId))
            }, onSessionArrived = { steamId, clientId ->
                navController.navigate(Routes.ConfirmSignIn.mapArgs(mapOf(
                    CommonArguments.SteamId to steamId,
                    Arguments.ClientId to clientId,
                )))
            }, onQrClicked = { steamId ->
                navController.navigate(Routes.ScanQrCode.withSteamId(steamId))
            })
        }

        composable(Routes.Setup.url, arguments = listOf(
            CommonArguments.SteamId,
            Arguments.GuardData
        )) {
            GuardSetupScreen(onBackClicked = navController::popBackStack, onSuccess = { steamId ->
                navController.navigate(Routes.Recovery.withSteamId(steamId.steamId))
            })
        }

        composable(Routes.Move.url, arguments = listOf(CommonArguments.SteamId)) {
            GuardMoveScreen(onBackClicked = navController::popBackStack, onSuccess = { steamId ->
                navController.navigate(Routes.Recovery.withSteamId(steamId.steamId))
            })
        }

        composable(Routes.Recovery.url, arguments = listOf(CommonArguments.SteamId)) {
            GuardRecoveryCodeScreen(onBackClicked = {
                navController.navigateToRoot()
            })
        }

        composable(Routes.Sessions.url, arguments = listOf(CommonArguments.SteamId)) {
            GuardDevicesScreen(onBackClicked = navController::popBackStack, onSessionClicked = { steamId, tokenDesc ->
                navController.navigate(Routes.SessionInfo.mapArgs(mapOf(
                    CommonArguments.SteamId to steamId,
                    Arguments.SessionData to CAuthentication_RefreshToken_Enumerate_Response.RefreshTokenDescription.ADAPTER.encodeByteString(
                        tokenDesc
                    ).base64Url(),
                )))
            })
        }

        composable(Routes.SessionInfo.url, arguments = listOf(
            CommonArguments.SteamId,
            Arguments.SessionData
        )) {
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
            QrSignScreen(onBackClicked = navController::popBackStack)
        }

        composable(Routes.Confirmations.url, arguments = listOf(CommonArguments.SteamId)) {
            ConfirmationsScreen(onBackClicked = navController::popBackStack, onConfirmationClicked = { steamId, confirmation ->
                navController.navigate(Routes.ConfirmationDetail.mapArgs(mapOf(
                    CommonArguments.SteamId to steamId,
                    Arguments.ConfirmationData to confirmation
                )))
            }, backStackEntry = it)
        }

        composable(Routes.ConfirmationDetail.url, arguments = listOf(CommonArguments.SteamId, Arguments.ConfirmationData)) {
            ConfirmationDetailScreen(onBackClicked = navController::popBackStack, onFinish = { event ->
                navController.setResultToPreviousEntry(event)
            })
        }

        bottomSheet(Routes.MoreOptions.url, arguments = listOf(CommonArguments.SteamId)) {
            GuardMoreOptionsSheet(onDevicesClicked = { steamId ->
                navController.popBackStack()
                navController.navigate(Routes.Sessions.withSteamId(steamId))
            }, onRemoveClicked = { steamId ->
                navController.popBackStack()
                navController.navigate(Routes.Remove.withSteamId(steamId))
            }, onRecoveryClicked = { steamId ->
                navController.popBackStack()
                navController.navigate(Routes.Recovery.withSteamId(steamId))
            }, onConfirmationsClicked = { steamId ->
                navController.popBackStack()
                navController.navigate(Routes.Confirmations.withSteamId(steamId))
            })
        }

        bottomSheet(Routes.ConfirmSignIn.url, arguments = listOf(
            CommonArguments.SteamId,
            Arguments.ClientId
        )) {
            GuardConfirmSessionSheet(onFinish = navController::popBackStack)
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
        icon = { Icons.Rounded.Security }
    )
}
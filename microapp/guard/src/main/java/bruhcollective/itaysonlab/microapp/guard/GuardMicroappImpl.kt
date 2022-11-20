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
import bruhcollective.itaysonlab.microapp.guard.ui.GuardScreen
import bruhcollective.itaysonlab.microapp.guard.ui.bottomsheet.GuardConfirmSessionSheet
import bruhcollective.itaysonlab.microapp.guard.ui.bottomsheet.GuardMoreOptionsSheet
import bruhcollective.itaysonlab.microapp.guard.ui.bottomsheet.GuardRemoveSheet
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
        composable(InternalRoutes.MainScreen.url) {
            GuardScreen(onMoveClicked = { steamId ->
                navController.navigate(InternalRoutes.Move.withSteamId(steamId))
            }, onAddClicked = { steamId, data ->
                navController.navigate(InternalRoutes.Setup.mapArgs(mapOf(
                    CommonArguments.SteamId to steamId,
                    Arguments.GuardData to CTwoFactor_AddAuthenticator_Response.ADAPTER.encodeByteString(
                        data
                    ).base64Url()
                )))
            }, onMoreClicked = { steamId ->
                navController.navigate(InternalRoutes.MoreOptions.withSteamId(steamId))
            }, onSessionArrived = { steamId, clientId ->
                navController.navigate(InternalRoutes.ConfirmSignIn.mapArgs(mapOf(
                    CommonArguments.SteamId to steamId,
                    Arguments.ClientId to clientId,
                )))
            }, onQrClicked = { steamId ->
                navController.navigate(InternalRoutes.ScanQrCode.withSteamId(steamId))
            })
        }

        composable(InternalRoutes.Setup.url, arguments = listOf(
            CommonArguments.SteamId,
            Arguments.GuardData
        )) {
            GuardSetupScreen(onBackClicked = navController::popBackStack, onSuccess = { steamId ->
                navController.navigate(InternalRoutes.Recovery.withSteamId(steamId.steamId))
            })
        }

        composable(InternalRoutes.Move.url, arguments = listOf(CommonArguments.SteamId)) {
            GuardMoveScreen(onBackClicked = navController::popBackStack, onSuccess = { steamId ->
                navController.navigate(InternalRoutes.Recovery.withSteamId(steamId.steamId))
            })
        }

        composable(InternalRoutes.Recovery.url, arguments = listOf(CommonArguments.SteamId)) {
            GuardRecoveryCodeScreen(onBackClicked = {
                navController.navigateToRoot()
            })
        }

        composable(InternalRoutes.Sessions.url, arguments = listOf(CommonArguments.SteamId)) {
            GuardDevicesScreen(onBackClicked = navController::popBackStack, onSessionClicked = { steamId, tokenDesc ->
                navController.navigate(InternalRoutes.SessionInfo.mapArgs(mapOf(
                    CommonArguments.SteamId to steamId,
                    Arguments.SessionData to CAuthentication_RefreshToken_Enumerate_Response.RefreshTokenDescription.ADAPTER.encodeByteString(
                        tokenDesc
                    ).base64Url(),
                )))
            })
        }

        composable(InternalRoutes.SessionInfo.url, arguments = listOf(
            CommonArguments.SteamId,
            Arguments.SessionData
        )) {
            GuardSessionScreen(onBackClicked = navController::popBackStack)
        }

        composable(InternalRoutes.ScanQrCode.url, arguments = listOf(CommonArguments.SteamId), enterTransition = {
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

        bottomSheet(InternalRoutes.MoreOptions.url, arguments = listOf(CommonArguments.SteamId)) {
            GuardMoreOptionsSheet(onDevicesClicked = { steamId ->
                navController.popBackStack()
                navController.navigate(InternalRoutes.Sessions.withSteamId(steamId))
            }, onRemoveClicked = { steamId ->
                navController.popBackStack()
                navController.navigate(InternalRoutes.Remove.withSteamId(steamId))
            }, onRecoveryClicked = { steamId ->
                navController.popBackStack()
                navController.navigate(InternalRoutes.Recovery.withSteamId(steamId))
            })
        }

        bottomSheet(InternalRoutes.ConfirmSignIn.url, arguments = listOf(
            CommonArguments.SteamId,
            Arguments.ClientId
        )) {
            GuardConfirmSessionSheet(onFinish = navController::popBackStack)
        }

        bottomSheet(InternalRoutes.Remove.url, arguments = listOf(CommonArguments.SteamId)) {
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
        navigate(InternalRoutes.MainScreen.url) {
            popUpTo(ROOT_NAV_GRAPH_ID)
        }
    }

    override val bottomNavigationEntry = NavigationEntry(
        route = InternalRoutes.NavGraph,
        name = R.string.guard,
        icon = { Icons.Rounded.Security }
    )
}
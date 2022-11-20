package bruhcollective.itaysonlab.microapp.profile

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.collectAsState
import androidx.navigation.*
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.find
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import bruhcollective.itaysonlab.microapp.gamepage.GamePageMicroapp
import bruhcollective.itaysonlab.microapp.library.LibraryMicroapp
import bruhcollective.itaysonlab.microapp.profile.ui.ProfileScreen
import bruhcollective.itaysonlab.microapp.profile.ui.screens.edit.ProfileEditScreen
import bruhcollective.itaysonlab.microapp.profile.ui.screens.editsections.ProfileEditSectionScreen
import bruhcollective.itaysonlab.microapp.profile.ui.screens.friends.FriendsScreen
import com.google.accompanist.navigation.animation.composable
import javax.inject.Inject

class ProfileMicroappImpl @Inject constructor() : ProfileMicroapp() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun NavGraphBuilder.buildGraph(
        navController: NavHostController,
        destinations: Destinations
    ) {
        composable(Routes.Profile.url, arguments = listOf(CommonArguments.SteamIdWithDefault)) {
            val reloadSign = it.savedStateHandle.getStateFlow(CommonArguments.ForceReload.name, false).collectAsState()

            ProfileScreen(
                onGameClick = { appId ->
                    navController.navigateToGame(destinations, appId)
                }, onLibraryClick = { steamId ->
                    navController.navigate(destinations.find<LibraryMicroapp>().libraryOf(steamId))
                }, onFriendsClick = { steamId ->
                    navController.navigate(friendsDestination(steamId))
                }, onEditClick = { steamId ->
                    navController.navigate(editDestination(steamId))
                }, onNavigationClick = { isRoot ->
                    if (isRoot) {

                    } else {
                        navController.popBackStack()
                    }
                }, reloadFlag = reloadSign.value, onReloadFlagTriggered = {
                    it.savedStateHandle[CommonArguments.ForceReload.name] = false
                }
            )
        }

        composable(Routes.Friends.url, arguments = listOf(CommonArguments.SteamId)) {
            FriendsScreen(
                onFriendClick = { steamId ->
                    navController.navigate(profileDestination(steamId))
                }, onBackClick = navController::popBackStack
            )
        }

        composable(Routes.Edit.url, arguments = listOf(CommonArguments.SteamId)) {
            val reloadSign = it.savedStateHandle.getStateFlow(CommonArguments.ForceReload.name, false).collectAsState()

            ProfileEditScreen(onBackClicked = navController::popBackStack, onSectionNavigate = { steamId, section ->
                navController.navigate(editDestination(steamId, section))
            }, reloadFlag = reloadSign.value, onReloadFlagTriggered = {
                navController.previousBackStackEntry?.savedStateHandle?.set(CommonArguments.ForceReload.name, true)
                it.savedStateHandle[CommonArguments.ForceReload.name] = false
            })
        }

        composable(Routes.EditSection.url, arguments = listOf(CommonArguments.SteamId, Arguments.SectionType)) {
            ProfileEditSectionScreen(onBackClicked = navController::popBackStack, onChangesCommitted = {
                navController.previousBackStackEntry?.savedStateHandle?.set(CommonArguments.ForceReload.name, true)
                navController.popBackStack()
            })
        }
    }

    private fun NavController.navigateToGame(destinations: Destinations, appId: Int) {
        navigate(
            destinations.find<GamePageMicroapp>()
                .gameDestination(appId)
        )
    }
}
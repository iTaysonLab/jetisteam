package bruhcollective.itaysonlab.microapp.profile

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.mapArgs
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import bruhcollective.itaysonlab.microapp.profile.ui.ProfileScreen
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import javax.inject.Inject

class ProfileMicroappImpl @Inject constructor() : ProfileMicroapp() {
    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
    override fun NavGraphBuilder.buildGraph(
        navController: NavHostController,
        destinations: Destinations
    ) {
        composable(Routes.Profile.url, arguments = listOf(CommonArguments.SteamIdWithDefault)) {
            ProfileScreen(
                onGameClick = { appId ->
                    // navController.navigate(destinations.find<GamePageMicroapp>().gameDestination(appId))
                }, onLibraryClick = { steamId ->
                    // navController.navigate(destinations.find<LibraryMicroapp>().libraryOf(steamId))
                }, onFriendsClick = { steamId ->
                    navController.navigate(friendsDestination(steamId))
                }, onNavigationClick = { isRoot, steamId ->
                    if (isRoot) {
                        navController.navigate(Routes.AppBottomSheet.mapArgs(mapOf(
                            CommonArguments.SteamId to steamId
                        )))
                    } else {
                        navController.popBackStack()
                    }
                }, backStackEntry = it
            )
        }

        /*composable(Routes.Friends.url, arguments = listOf(CommonArguments.SteamId)) {
            FriendsScreen(
                onFriendClick = { steamId ->
                    navController.navigate(profileDestination(steamId))
                }, onBackClick = navController::popBackStack
            )
        }

        composable(Routes.Edit.url, arguments = listOf(CommonArguments.SteamId)) {
            ProfileEditScreen(onBackClicked = navController::popBackStack, onSectionNavigate = { steamId, section ->
                navController.navigate(editDestination(steamId, section))
            }, backStackEntry = it, onNavResultConsumed = { event ->
                navController.setResultToPreviousEntry(event, popBackStack = false)
            })
        }

        composable(Routes.EditSection.url, arguments = listOf(CommonArguments.SteamId, Arguments.SectionType)) {
            val onChangesCommitted = { event: ProfileEditEvent ->
                navController.setResultToPreviousEntry(event, popBackStack = true)
            }

            if (it.savedStateHandle.get<SectionType>(Arguments.SectionType.name) == SectionType.ProfileTheme) {
                ProfileEditThemeScreen(onBackClicked = navController::popBackStack, onChangesCommitted = onChangesCommitted)
            } else {
                ProfileEditSectionScreen(onBackClicked = navController::popBackStack, onChangesCommitted = onChangesCommitted)
            }
        }

        bottomSheet(Routes.AppBottomSheet.url) {
            GlobalAppBottomSheet(onBackClicked = navController::popBackStack, onEditProfileClicked = { steamId ->
                navController.popBackStack()
                navController.navigate(editDestination(steamId))
            }, onSteamWrappedClicked = { steamId ->
                navController.popBackStack()
                // navController.navigate(destinations.find<SteamWrappedMicroapp>().entryDestination(steamId))
            })
        }*/
    }
}
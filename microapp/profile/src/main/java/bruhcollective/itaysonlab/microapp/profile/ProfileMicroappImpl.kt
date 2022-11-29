package bruhcollective.itaysonlab.microapp.profile

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.find
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import bruhcollective.itaysonlab.microapp.core.navigation.extensions.results.setResultToPreviousEntry
import bruhcollective.itaysonlab.microapp.gamepage.GamePageMicroapp
import bruhcollective.itaysonlab.microapp.library.LibraryMicroapp
import bruhcollective.itaysonlab.microapp.profile.core.ProfileEditEvent
import bruhcollective.itaysonlab.microapp.profile.core.SectionType
import bruhcollective.itaysonlab.microapp.profile.ui.ProfileScreen
import bruhcollective.itaysonlab.microapp.profile.ui.bottomsheet.GlobalAppBottomSheet
import bruhcollective.itaysonlab.microapp.profile.ui.screens.edit.ProfileEditScreen
import bruhcollective.itaysonlab.microapp.profile.ui.screens.editsections.ProfileEditSectionScreen
import bruhcollective.itaysonlab.microapp.profile.ui.screens.editsections.ProfileEditThemeScreen
import bruhcollective.itaysonlab.microapp.profile.ui.screens.friends.FriendsScreen
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
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
                    navController.navigate(destinations.find<GamePageMicroapp>().gameDestination(appId))
                }, onLibraryClick = { steamId ->
                    navController.navigate(destinations.find<LibraryMicroapp>().libraryOf(steamId))
                }, onFriendsClick = { steamId ->
                    navController.navigate(friendsDestination(steamId))
                }, onEditClick = { steamId ->
                    navController.navigate(editDestination(steamId))
                }, onNavigationClick = { isRoot ->
                    if (isRoot) {
                        navController.navigate(Routes.AppBottomSheet.url)
                    } else {
                        navController.popBackStack()
                    }
                }, backStackEntry = it
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
            GlobalAppBottomSheet(onBackClicked = navController::popBackStack)
        }
    }
}
package bruhcollective.itaysonlab.microapp.profile

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.*
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.find
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import bruhcollective.itaysonlab.microapp.gamepage.GamePageMicroapp
import bruhcollective.itaysonlab.microapp.library.LibraryMicroapp
import bruhcollective.itaysonlab.microapp.profile.ui.ProfileScreen
import bruhcollective.itaysonlab.microapp.profile.ui.screens.friends.FriendsScreen
import com.google.accompanist.navigation.animation.composable
import javax.inject.Inject

class ProfileMicroappImpl @Inject constructor(): ProfileMicroapp() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun NavGraphBuilder.buildGraph(
        navController: NavHostController,
        destinations: Destinations
    ) {
        composable(InternalRoutes.Profile.url, arguments = listOf(CommonArguments.SteamIdWithDefault)) {
            ProfileScreen(
                onGameClick = { appId ->
                    navController.navigateToGame(destinations, appId)
                }, onLibraryClick = { steamId ->
                    navController.navigate(destinations.find<LibraryMicroapp>().libraryOf(steamId))
                }, onFriendsClick = { steamId ->
                    navController.navigate(
                        friendsDestination(steamId)
                    )
                }, onBackClick = if (navController.backQueue.size > 3) {
                    { navController.popBackStack() }
                } else null
            )
        }

        composable(InternalRoutes.Friends.url, arguments = listOf(CommonArguments.SteamId)) {
            FriendsScreen(
                onFriendClick = { steamId ->
                    navController.navigate(
                        profileDestination(steamId)
                    )
                }, onBackClick = navController::popBackStack
            )
        }
    }

    private fun NavController.navigateToGame(destinations: Destinations, appId: Int) {
        navigate(
            destinations.find<GamePageMicroapp>()
                .gameDestination(appId)
        )
    }
}
package bruhcollective.itaysonlab.microapp.profile

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import com.google.accompanist.navigation.animation.composable
import androidx.navigation.navigation
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.NavigationEntry
import bruhcollective.itaysonlab.microapp.gamepage.GamePageMicroapp
import bruhcollective.itaysonlab.microapp.library.LibraryMicroapp
import bruhcollective.itaysonlab.microapp.profile.ui.ProfileScreen
import bruhcollective.itaysonlab.microapp.profile.ui.screens.friends.FriendsScreen
import javax.inject.Inject

class ProfileMicroappImpl @Inject constructor(): ProfileMicroapp() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun NavGraphBuilder.navigation(
        navController: NavHostController,
        destinations: Destinations
    ) {
        navigation(startDestination = myProfileDestination(), route = InternalRoutes.NavGraph) {
            composable(InternalRoutes.Profile) {
                ProfileScreen(
                    onGameClick = { appId ->
                        navController.navigateToGame(destinations, appId)
                    }, onLibraryClick = { steamId ->
                        navController.navigate(LibraryMicroapp.libraryOf(steamId))
                    }, onFriendsClick = { steamId ->
                        navController.navigate(
                            friendsDestination(steamId)
                        )
                    }, onBackClick = if (navController.backQueue.size > 3) {
                        { navController.popBackStack() }
                    } else null
                )
            }

            composable(InternalRoutes.Friends) {
                FriendsScreen(
                    onFriendClick = { steamId ->
                        navController.navigate(
                            profileDestination(steamId)
                        )
                    }, onBackClick = navController::popBackStack
                )
            }
        }
    }

    private fun NavController.navigateToGame(destinations: Destinations, appId: Int) {
        navigate(GamePageMicroapp.gameDestination(appId))
    }

    override val bottomNavigationEntry = NavigationEntry(
        route = InternalRoutes.NavGraph,
        name = R.string.profile,
        icon = { Icons.Rounded.Person }
    )

    object InternalRoutes {
        const val NavGraph = "@profile"

        const val ARG_ID = "steam_id"
        const val ARG_MY_PROFILE = "my"

        const val Profile = "profile/{$ARG_ID}"
        const val Friends = "profile/{$ARG_ID}/friends"
    }
}
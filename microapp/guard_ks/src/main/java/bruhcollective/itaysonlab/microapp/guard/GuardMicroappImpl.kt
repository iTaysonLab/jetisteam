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
import bruhcollective.itaysonlab.microapp.guard.ui.setup.GuardSetupScreen
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
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
            })
        }

        composable(Routes.Setup.url, arguments = listOf(CommonArguments.SteamId)) {
            GuardSetupScreen(onBackClicked = navController::popBackStack, onSuccess = { steamId ->
                navController.navigateToRoot()
            })
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
package bruhcollective.itaysonlab.microapp.steam_wrapped

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.navigation.CommonArguments
import bruhcollective.itaysonlab.microapp.steam_wrapped.ui.SteamReplayScreen
import com.google.accompanist.navigation.animation.composable
import javax.inject.Inject

class SteamWrappedMicroappImpl @Inject constructor() : SteamWrappedMicroapp() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun NavGraphBuilder.buildGraph(
        navController: NavHostController,
        destinations: Destinations
    ) {
        composable(
            Routes.Main.url,
            arguments = listOf(CommonArguments.SteamIdWithDefault)
        ) {
            SteamReplayScreen(onBackPressed = navController::popBackStack)
        }
    }
}
package bruhcollective.itaysonlab.microapp.gamepage

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.mapArgs
import bruhcollective.itaysonlab.microapp.gamepage.ui.GamePageScreen
import bruhcollective.itaysonlab.microapp.gamepage.ui.achievements.GameAchievementsScreen
import bruhcollective.itaysonlab.microapp.gamepage.ui.bottomsheet.DeckReportBottomSheet
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet
import javax.inject.Inject

class GamePageMicroappImpl @Inject constructor() : GamePageMicroapp() {
    @OptIn(ExperimentalAnimationApi::class, ExperimentalMaterialNavigationApi::class)
    override fun NavGraphBuilder.buildGraph(
        navController: NavHostController,
        destinations: Destinations
    ) {
        composable(Routes.Game.url, arguments = listOf(Arguments.GameId)) {
            GamePageScreen(onBackClick = navController::popBackStack, onDeckReportClicked = { appId ->
                navController.navigate(Routes.DeckReport.mapArgs(mapOf(
                    Arguments.GameId to appId
                )))
            })
        }

        composable(Routes.GameAchievements.url, arguments = listOf(Arguments.GameId)) {
            GameAchievementsScreen(onBackClick = navController::popBackStack)
        }

        bottomSheet(Routes.DeckReport.url, arguments = listOf(Arguments.GameId)) {
            DeckReportBottomSheet(onBackClick = navController::popBackStack)
        }
    }
}
package bruhcollective.itaysonlab.microapp.auth

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.dialog
import bruhcollective.itaysonlab.microapp.auth.ui.*
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.find
import bruhcollective.itaysonlab.microapp.core.mapArgs
import bruhcollective.itaysonlab.microapp.profile.ProfileMicroapp
import com.google.accompanist.navigation.animation.composable
import javax.inject.Inject

class AuthMicroappImpl @Inject constructor(): AuthMicroapp() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun NavGraphBuilder.buildGraph(
        navController: NavHostController,
        destinations: Destinations
    ) {
        composable(InternalRoutes.MainScreen.url) {
            AuthScreen(onOpenDisclaimer = {
                navController.navigate(InternalRoutes.AuthDisclaimer.url)
            }, onProceedToNextStep = { hasMobileAuth ->
                navController.navigate(InternalRoutes.TfaScreen.mapArgs(mapOf(
                    Arguments.MobileAuthEnabled to hasMobileAuth
                )))
            })
        }

        composable(InternalRoutes.TfaScreen.url) {
            TfaScreen(onSuccess = {
                navController.navigate(
                    destinations.find<ProfileMicroapp>().myProfileDestination()
                )
            })
        }

        dialog(InternalRoutes.AuthDisclaimer.url) {
            AuthDisclaimer(onBackPressed = navController::popBackStack)
        }
    }
}
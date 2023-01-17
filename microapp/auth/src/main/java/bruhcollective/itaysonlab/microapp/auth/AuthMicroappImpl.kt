package bruhcollective.itaysonlab.microapp.auth

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import bruhcollective.itaysonlab.microapp.auth.ui.*
import bruhcollective.itaysonlab.microapp.core.Destinations
import bruhcollective.itaysonlab.microapp.core.find
import bruhcollective.itaysonlab.microapp.notifications.NotificationsMicroapp
import com.google.accompanist.navigation.animation.composable
import javax.inject.Inject

class AuthMicroappImpl @Inject constructor(): AuthMicroapp() {
    @OptIn(ExperimentalAnimationApi::class)
    override fun NavGraphBuilder.buildGraph(
        navController: NavHostController,
        destinations: Destinations
    ) {
        composable(Routes.MainScreen.url) {
            OnboardingScreen(onSignClicked = {
                navController.navigate(Routes.SignInScreen.url)
            }, onQrClicked = {
                navController.navigate(Routes.QrCodeScreen.url)
            })
        }

        composable(Routes.SignInScreen.url) {
            AuthScreen(onProceedToNextStep = {
                navController.navigate(Routes.TfaScreen.url)
            }, onBackClicked = navController::popBackStack)
        }

        composable(Routes.TfaScreen.url) {
            TfaScreen(onSuccess = {
                navController.navigate(destinations.find<NotificationsMicroapp>().microappRoute)
            }, onCancel = navController::popBackStack)
        }
    }
}
package bruhcollective.itaysonlab.cobalt.navigation

import androidx.compose.runtime.Composable
import bruhcollective.itaysonlab.cobalt.cobaltStackAnimator
import bruhcollective.itaysonlab.cobalt.guard.GuardRootScreen
import bruhcollective.itaysonlab.cobalt.guard.confirmation.GuardConfirmationPage
import bruhcollective.itaysonlab.cobalt.guard.session.GuardSessionScreen
import bruhcollective.itaysonlab.cobalt.guard.setup.recovery.GuardSaveCodeScreen
import bruhcollective.itaysonlab.cobalt.guard.setup.sms.GuardEnterSmsScreen
import bruhcollective.itaysonlab.cobalt.library.LibraryScreen
import bruhcollective.itaysonlab.cobalt.news.discover.DiscoverScreen
import bruhcollective.itaysonlab.cobalt.profile.ProfileScreen
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatable
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun DestinationScreen (
    component: DestinationComponent
) {
    Children(
        stack = component.stack,
        animation = predictiveBackAnimation(
            backHandler = component.backHandler,
            fallbackAnimation = stackAnimation(cobaltStackAnimator()),
            selector = { backEvent, _, _ -> androidPredictiveBackAnimatable(backEvent) },
            onBack = component::onBackPressed
        )
    ) { stackChild ->
        when (val child = stackChild.instance) {
            is DestinationChild.GuardRoot -> {
                GuardRootScreen(child.component)
            }

            is DestinationChild.GuardConfirmationDetail -> {
                GuardConfirmationPage(child.component)
            }

            is DestinationChild.GuardSessionDetail -> {
                GuardSessionScreen(child.component)
            }

            is DestinationChild.GuardSetupEnterCode -> {
                GuardEnterSmsScreen(child.component)
            }

            is DestinationChild.GuardSetupRecoveryCode -> {
                GuardSaveCodeScreen(child.component)
            }

            is DestinationChild.LibraryRoot -> {
                LibraryScreen(child.component)
            }

            is DestinationChild.Newsfeed -> {
                DiscoverScreen(child.component)
            }

            is DestinationChild.Profile -> {
                ProfileScreen(child.component)
            }
        }
    }
}
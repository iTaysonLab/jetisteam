package bruhcollective.itaysonlab.cobalt.screens.navigation

import androidx.compose.runtime.Composable
import bruhcollective.itaysonlab.cobalt.cobaltStackAnimator
import bruhcollective.itaysonlab.cobalt.screens.guard.GuardRootScreen
import bruhcollective.itaysonlab.cobalt.screens.guard.confirmation.GuardConfirmationPage
import bruhcollective.itaysonlab.cobalt.screens.guard.session.GuardSessionScreen
import bruhcollective.itaysonlab.cobalt.screens.guard.setup.recovery.GuardSaveCodeScreen
import bruhcollective.itaysonlab.cobalt.screens.guard.setup.sms.GuardEnterSmsScreen
import bruhcollective.itaysonlab.cobalt.screens.library.LibraryScreen
import bruhcollective.itaysonlab.cobalt.navigation.DestinationChild
import bruhcollective.itaysonlab.cobalt.navigation.DestinationComponent
import bruhcollective.itaysonlab.cobalt.screens.library.games.achievements.AchievementsScreen
import bruhcollective.itaysonlab.cobalt.screens.news.NewsfeedScreen
import bruhcollective.itaysonlab.cobalt.screens.news.WrappedNewsfeedScreen
import bruhcollective.itaysonlab.cobalt.screens.profile.ProfileScreen
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

            is DestinationChild.WrappedNewsfeed -> {
                WrappedNewsfeedScreen(child.component)
            }

            is DestinationChild.Newsfeed -> {
                NewsfeedScreen(child.component)
            }

            is DestinationChild.Profile -> {
                ProfileScreen(child.component)
            }

            is DestinationChild.AppAchievements -> {
                AchievementsScreen(child.component, component::onBackPressed)
            }
        }
    }
}
package bruhcollective.itaysonlab.cobalt.guard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import bruhcollective.itaysonlab.cobalt.cobaltStackAnimator
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardConfirmSessionSheet
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardRecoveryCodeSheet
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardRemoveSheet
import bruhcollective.itaysonlab.cobalt.guard.bottom_sheet.GuardSetupOverrideExistingSheet
import bruhcollective.itaysonlab.cobalt.guard.confirmation.GuardConfirmationPage
import bruhcollective.itaysonlab.cobalt.guard.instance.GuardInstanceScreen
import bruhcollective.itaysonlab.cobalt.guard.qr_code.GuardQrCodeSheet
import bruhcollective.itaysonlab.cobalt.guard.session.GuardSessionScreen
import bruhcollective.itaysonlab.cobalt.guard.setup.onboarding.GuardOnboardingScreen
import bruhcollective.itaysonlab.cobalt.guard.setup.recovery.GuardSaveCodeScreen
import bruhcollective.itaysonlab.cobalt.guard.setup.sms.GuardEnterSmsScreen
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.androidPredictiveBackAnimatable
import com.arkivanov.decompose.extensions.compose.stack.animation.predictiveback.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun GuardScreen(
    component: GuardComponent,
) {
    val stack by component.stack.subscribeAsState()
    val alert by component.alertSlot.subscribeAsState()

    alert.child?.instance?.let {
        when (val child = it) {
            is GuardComponent.AlertChild.SetupOverrideExisting -> {
                GuardSetupOverrideExistingSheet(child.component)
            }

            is GuardComponent.AlertChild.InstanceDelete -> {
                GuardRemoveSheet(child.component)
            }

            is GuardComponent.AlertChild.InstanceRecoveryCode -> {
                GuardRecoveryCodeSheet(child.component)
            }

            is GuardComponent.AlertChild.IncomingSession -> {
                GuardConfirmSessionSheet(child.component)
            }

            is GuardComponent.AlertChild.QrCodeScanner -> {
                GuardQrCodeSheet(child.component)
            }
        }
    }

    Children(
        stack = stack,
        animation = predictiveBackAnimation(
            backHandler = component.backHandler,
            fallbackAnimation = stackAnimation(cobaltStackAnimator()),
            selector = { backEvent, _, _ -> androidPredictiveBackAnimatable(backEvent) },
            onBack = component::onBackPressed
        )
    ) {
        when (val child = it.instance) {
            is GuardComponent.Child.SetupOnboarding -> {
                GuardOnboardingScreen(child.component)
            }

            is GuardComponent.Child.SetupEnterSmsCode -> {
                GuardEnterSmsScreen(child.component)
            }

            is GuardComponent.Child.SetupRecoveryCode -> {
                GuardSaveCodeScreen(child.component)
            }

            is GuardComponent.Child.Instance -> {
                GuardInstanceScreen(child.component)
            }

            is GuardComponent.Child.ActiveSessionDetail -> {
                GuardSessionScreen(child.component)
            }

            is GuardComponent.Child.MobileConfirmationDetail -> {
                GuardConfirmationPage(child.component)
            }
        }
    }
}
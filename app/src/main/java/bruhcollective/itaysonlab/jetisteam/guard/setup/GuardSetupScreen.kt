package bruhcollective.itaysonlab.jetisteam.guard.setup

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.sharp.Cancel
import androidx.compose.material.icons.sharp.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import bruhcollective.itaysonlab.cobalt.guard.setup.GuardSetupComponent
import bruhcollective.itaysonlab.jetisteam.R
import bruhcollective.itaysonlab.jetisteam.guard.setup.onboarding.GuardOnboardingScreen
import bruhcollective.itaysonlab.jetisteam.guard.setup.recovery.GuardSaveCodeScreen
import bruhcollective.itaysonlab.jetisteam.guard.setup.sms.GuardEnterSmsScreen
import bruhcollective.itaysonlab.jetisteam.ui.components.EmptyWindowInsets
import bruhcollective.itaysonlab.jetisteam.ui.components.InlineMonoButton
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuardSetupScreen(
    component: GuardSetupComponent,
    topPadding: Dp
) {
    val alert by component.alert.subscribeAsState()

    alert.child?.instance?.let {
        when (val child = it) {
            is GuardSetupComponent.AlertChild.GuardAlreadyExists -> {
                ModalBottomSheet(
                    onDismissRequest = component::onAlertDismissed,
                    tonalElevation = 0.dp,
                    shape = RectangleShape,
                    containerColor = MaterialTheme.colorScheme.background,
                    windowInsets = EmptyWindowInsets
                ) {
                    Text(
                        text = stringResource(id = R.string.guard_setup_move),
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = stringResource(id = R.string.guard_setup_move_desc),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                    InlineMonoButton(
                        icon = {
                            Icon(
                                imageVector = Icons.Sharp.Check,
                                contentDescription = null
                            )
                        },
                        title = "Confirm migration",
                        onClick = child.component::onConfirm,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.primary
                        )
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                    InlineMonoButton(
                        icon = {
                            Icon(
                                imageVector = Icons.Sharp.Cancel,
                                contentDescription = null
                            )
                        },
                        title = "Cancel",
                        onClick = child.component::onCancel,
                        colors = ButtonDefaults.textButtonColors(
                            contentColor = MaterialTheme.colorScheme.onSurface
                        )
                    )

                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)

                    Spacer(modifier = Modifier.navigationBarsPadding())
                }
            }
        }
    }

    Children(
        stack = component.childStack,
        animation = stackAnimation(fade() + slide()),
    ) {
        when (val child = it.instance) {
            is GuardSetupComponent.Child.Onboarding -> GuardOnboardingScreen(child.component, topPadding)
            is GuardSetupComponent.Child.EnterSmsCode -> GuardEnterSmsScreen(child.component, topPadding)
            is GuardSetupComponent.Child.SaveRecoveryCode -> GuardSaveCodeScreen(child.component, topPadding)
        }
    }
}